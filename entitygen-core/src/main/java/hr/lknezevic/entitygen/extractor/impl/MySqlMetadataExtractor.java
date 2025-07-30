package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
public class MySqlMetadataExtractor implements MetadataExtractor {
    private final SchemaFilter schemaFilter;

    @Override
    public List<Schema> extractSchemas(Connection connection) {
        List<Schema> schemas = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rsCatalogs = metaData.getCatalogs();

            log.debug("Fetching catalogs (databases) from metadata...");

            while (rsCatalogs.next()) {
                String catalogName = rsCatalogs.getString("TABLE_CAT");
                log.debug("Found catalog: {}", catalogName);

                if (schemaFilter.isSystemSchema(catalogName)) {
                    log.debug("Skipping system schema: {}", catalogName);
                    continue;
                }

                List<Table> tables = extractTables(metaData, catalogName);

                Schema schema = Schema.builder()
                        .name(catalogName)
                        .tables(tables)
                        .build();

                schemas.add(schema);
                log.info("Added schema: {}", catalogName);
            }

            log.info("Schema extraction completed. Total schemas: {}", schemas.size());
        } catch (Exception e) {
            log.error("Error extracting schemas", e);
            throw new RuntimeException(e);
        }

        return schemas;
    }

    @Override
    public List<Table> extractTables(DatabaseMetaData metaData, String schemaName) {
        List<Table> tables = new ArrayList<>();

        try (ResultSet rsTables = metaData.getTables(schemaName, null, "%", new String[]{"TABLE"})) {
            log.debug("Fetching tables for schema: {}", schemaName);

            while (rsTables.next()) {
                String tableName = rsTables.getString("TABLE_NAME");
                String tableType = rsTables.getString("TABLE_TYPE");

                if (!"TABLE".equalsIgnoreCase(tableType)) continue;

                log.info("Found table: {}", tableName);

                List<Column> columns = extractColumns(metaData, schemaName, tableName);
                List<ForeignKey> foreignKeys = extractForeignKeys(metaData, schemaName, tableName);
                List<UniqueConstraint> uniqueConstraints = extractUniqueConstraints(metaData, schemaName, tableName);

                Table table = Table.builder()
                        .name(tableName)
                        .schema(schemaName)
                        .catalog(schemaName)
                        .columns(columns)
                        .foreignKeys(foreignKeys)
                        .uniqueConstraints(uniqueConstraints)
                        .build();

                tables.add(table);
            }

        } catch (Exception e) {
            log.error("Error extracting tables", e);
            throw new RuntimeException(e);
        }

        return tables;
    }

    @Override
    public List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName) {
        List<Column> columns = new ArrayList<>();

        Set<String> primaryKeys = new HashSet<>();
        try (ResultSet rsPK = metaData.getPrimaryKeys(schemaName, null, tableName)) {
            while (rsPK.next()) {
                primaryKeys.add(rsPK.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            log.warn("Error retrieving primary keys for {}.{}", schemaName, tableName, e);
        }

        Set<String> uniqueColumns = new HashSet<>();
        try (ResultSet rsIndex = metaData.getIndexInfo(schemaName, null, tableName, true, false)) {
            while (rsIndex.next()) {
                String columnName = rsIndex.getString("COLUMN_NAME");
                if (columnName != null) {
                    uniqueColumns.add(columnName);
                }
            }
        } catch (SQLException e) {
            log.warn("Error retrieving unique columns for {}.{}", schemaName, tableName, e);
        }

        try (ResultSet rs = metaData.getColumns(schemaName, null, tableName, "%")) {
            while (rs.next()) {
                String columnName = rs.getString("COLUMN_NAME");
                log.debug("Found column: {}", columnName);

                int dataType = rs.getInt("DATA_TYPE");

                int columnSize = rs.getInt("COLUMN_SIZE");
                int scale = rs.getInt("DECIMAL_DIGITS");
                
                // Determine length vs precision based on data type
                Integer length = null;
                Integer precision = null;
                
                // String types use length
                if (dataType == Types.VARCHAR || dataType == Types.CHAR || 
                    dataType == Types.LONGVARCHAR || dataType == Types.CLOB) {
                    length = columnSize > 0 ? columnSize : null;
                }
                // Numeric types use precision
                else if (dataType == Types.NUMERIC || dataType == Types.DECIMAL) {
                    precision = columnSize > 0 ? columnSize : null;
                }

                Column column = Column.builder()
                        .name(columnName)
                        .dataType(dataType)
                        .typeName(rs.getString("TYPE_NAME"))
                        .nullable("YES".equalsIgnoreCase(rs.getString("IS_NULLABLE")))
                        .primaryKey(primaryKeys.contains(columnName))
                        .autoIncrement("YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT")))
                        .unique(uniqueColumns.contains(columnName))
                        .defaultValue(rs.getString("COLUMN_DEF"))
                        .comment(rs.getString("REMARKS"))
                        .length(length)
                        .precision(precision)
                        .scale(scale > 0 ? scale : null)
                        .javaType(resolveJavaType(dataType))
                        .isLob(isLobType(dataType) || "TEXT".equalsIgnoreCase(rs.getString("TYPE_NAME")) || 
                               "LONGTEXT".equalsIgnoreCase(rs.getString("TYPE_NAME")) || 
                               "MEDIUMTEXT".equalsIgnoreCase(rs.getString("TYPE_NAME")))
                        .build();

                columns.add(column);
            }
        } catch (SQLException e) {
            log.error("Error extracting columns for {}.{}", schemaName, tableName, e);
            throw new RuntimeException(e);
        }

        return columns;
    }

    @Override
    public List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName) {
        List<ForeignKey> foreignKeys = new ArrayList<>();

        try (ResultSet rs = metaData.getImportedKeys(null, schemaName, tableName)) {
            while (rs.next()) {
                ForeignKey foreignKey = ForeignKey.builder()
                        .name(rs.getString("FK_NAME"))
                        .fkTable(rs.getString("FKTABLE_NAME"))
                        .fkColumn(rs.getString("FKCOLUMN_NAME"))
                        .referencedTable(rs.getString("PKTABLE_NAME"))
                        .referencedColumn(rs.getString("PKCOLUMN_NAME"))
                        .onDeleteAction(resolveAction(rs.getShort("DELETE_RULE")))
                        .onUpdateAction(resolveAction(rs.getShort("UPDATE_RULE")))
                        .onDeleteCascade(rs.getShort("DELETE_RULE") == DatabaseMetaData.importedKeyCascade)
                        .onUpdateCascade(rs.getShort("UPDATE_RULE") == DatabaseMetaData.importedKeyCascade)
                        .build();

                foreignKeys.add(foreignKey);
            }


        } catch (SQLException e) {
            log.error("Error extracting foreign key for {}.{}", schemaName, tableName, e);
            throw new RuntimeException(e);
        }

        return foreignKeys;
    }

    @Override
    public List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName) {
        Map<String, List<String>> constraintMap = new HashMap<>();

        try (ResultSet rs = metaData.getIndexInfo(null, schemaName, tableName, true, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                String columnName = rs.getString("COLUMN_NAME");

                if (indexName == null || columnName == null) continue;

                constraintMap.computeIfAbsent(indexName, k -> new ArrayList<>()).add(columnName);
            }
        } catch (SQLException e) {
            log.error("Error extracting unique constraint for {}.{}", schemaName, tableName, e);
            throw new RuntimeException(e);
        }

        List<UniqueConstraint> uniqueConstraints = new ArrayList<>();
        for (var entry : constraintMap.entrySet()) {
            uniqueConstraints.add(UniqueConstraint.builder()
                    .name(entry.getKey())
                    .columns(entry.getValue())
                    .build());
        }

        return uniqueConstraints;
    }
}
