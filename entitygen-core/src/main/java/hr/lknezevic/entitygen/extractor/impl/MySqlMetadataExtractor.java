package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.model.*;
import hr.lknezevic.entitygen.utils.MetadataExtractorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MySQL Metadata Extractor implementation for extracting database schema, tables, columns, foreign keys, and unique constraints.
 *
 * @author leonknezevic
 */
@Slf4j
@RequiredArgsConstructor
public class MySqlMetadataExtractor implements MetadataExtractor {
    private final SchemaFilter schemaFilter;

    /**
     * Extracts schemas (databases) from the given connection.ž
     *
     * @param connection the database connection
     * @return a list of schemas
     */
    @Override
    public List<Schema> extractSchemas(Connection connection) {
        List<Schema> schemas = new ArrayList<>();
        ResultSet rsCatalogs = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            rsCatalogs = metaData.getCatalogs();

            log.debug("Fetching catalogs (databases) from metadata...");

            while (rsCatalogs.next()) {
                String catalogName = rsCatalogs.getString("TABLE_CAT");
                log.debug("Found catalog: {}", catalogName);

                if (schemaFilter.isSystemSchema(catalogName) || !schemaFilter.isTargetSchema(catalogName)) {
                    log.debug("Skipping schema: {}", catalogName);
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
        } finally {
            if (rsCatalogs != null) {
                try {
                    rsCatalogs.close();
                } catch (SQLException e) {
                    log.warn("Failed to close ResultSet", e);
                }
            }
        }

        return schemas;
    }

    /**
     * Extracts tables from the given schema name.
     *
     * @param metaData the DatabaseMetaData object
     * @param schemaName the name of the schema to extract tables from
     * @return a list of tables in the specified schema
     */
    @Override
    public List<Table> extractTables(DatabaseMetaData metaData, String schemaName) {
        List<Table> tables = new ArrayList<>();

        try (ResultSet rsTables = metaData.getTables(schemaName, null, "%", new String[]{"TABLE"})) {
            log.debug("Fetching tables for schema: {}", schemaName);

            while (rsTables.next()) {
                String tableName = rsTables.getString("TABLE_NAME");

                if (!schemaFilter.isTableIncluded(tableName)) {
                    log.debug("Skipping table: {}", tableName);
                    continue;
                }

                String tableType = rsTables.getString("TABLE_TYPE");

                if (!"TABLE".equalsIgnoreCase(tableType)) continue;

                log.info("Found table: {}", tableName);

                List<Column> columns = extractColumns(metaData, schemaName, tableName);
                List<ForeignKey> foreignKeys = extractForeignKeys(metaData, schemaName, tableName);
                List<UniqueConstraint> uniqueConstraints = extractUniqueConstraints(metaData, schemaName, tableName);

                updateForeignKeys(foreignKeys, columns);

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

    /**
     * Extracts columns for a specific table in the given schema.
     *
     * @param metaData the DatabaseMetaData object
     * @param schemaName the name of the schema
     * @param tableName the name of the table
     * @return a list of columns in the specified table
     */
    @Override
    public List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName) {
        List<Column> columns = new ArrayList<>();

        Set<String> primaryKeys = new HashSet<>();
        try (ResultSet rsPK = metaData.getPrimaryKeys(schemaName, null, tableName)) {
            while (rsPK.next()) {
                String pkColumn = rsPK.getString("COLUMN_NAME");
                if (pkColumn != null) {
                    primaryKeys.add(pkColumn);
                }
            }
        } catch (SQLException e) {
            log.warn("Error retrieving primary keys for {}.{}", schemaName, tableName, e);
        }

        Set<String> uniqueColumns = new HashSet<>();
        try (ResultSet rsIndex = metaData.getIndexInfo(schemaName, null, tableName, true, false)) {
            while (rsIndex.next()) {
                String columnName = rsIndex.getString("COLUMN_NAME");
                if (columnName != null && !primaryKeys.contains(columnName)) {
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
                boolean isAutoIncrement = "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));

                if (isAutoIncrement && dataType == Types.INTEGER) {
                    dataType = Types.BIGINT;
                }

                int columnSize = rs.getInt("COLUMN_SIZE");
                int scale = rs.getInt("DECIMAL_DIGITS");

                Integer length = null;
                Integer precision = null;

                if (dataType == Types.VARCHAR || dataType == Types.CHAR || 
                    dataType == Types.LONGVARCHAR || dataType == Types.CLOB) {
                    length = columnSize > 0 ? columnSize : null;
                }

                else if (dataType == Types.NUMERIC || dataType == Types.DECIMAL) {
                    precision = columnSize > 0 ? columnSize : null;
                }

                Column column = Column.builder()
                        .name(columnName)
                        .dataType(dataType)
                        .javaType(MetadataExtractorUtil.resolveJavaType(dataType, precision, scale))
                        .nullable("YES".equalsIgnoreCase(rs.getString("IS_NULLABLE")))
                        .primaryKey(primaryKeys.contains(columnName))
                        .autoIncrement(isAutoIncrement)
                        .unique(uniqueColumns.contains(columnName))
                        .defaultValue(rs.getString("COLUMN_DEF"))
                        .comment(rs.getString("REMARKS"))
                        .length(length)
                        .precision(precision)
                        .scale(scale >= 0 ? scale : null)
                        .isLob(MetadataExtractorUtil.isLobType(rs, dataType))
                        .build();

                columns.add(column);
            }
        } catch (SQLException e) {
            log.error("Error extracting columns for {}.{}", schemaName, tableName, e);
            throw new RuntimeException(e);
        }

        return columns;
    }

    /**
     * Extracts foreign keys for a specific table in the given schema.
     *
     * @param metaData the DatabaseMetaData object
     * @param schemaName the name of the schema
     * @param tableName the name of the table
     * @return a list of foreign keys in the specified table
     */
    @Override
    public List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName) {
        List<ForeignKey> foreignKeys = new ArrayList<>();

        try (ResultSet rs = metaData.getImportedKeys(null, schemaName, tableName)) {
            while (rs.next()) {
                ForeignKey foreignKey = ForeignKey.builder()
                        .constraintName(rs.getString("FK_NAME"))
                        .fkColumn(rs.getString("FKCOLUMN_NAME"))
                        .referencedTable(rs.getString("PKTABLE_NAME"))
                        .referencedColumn(rs.getString("PKCOLUMN_NAME"))
                        .nullable(true)
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

    /**
     * Extracts unique constraints for a specific table in the given schema.
     *
     * @param metaData the DatabaseMetaData object
     * @param schemaName the name of the schema
     * @param tableName the name of the table
     * @return a list of unique constraints in the specified table
     */
    @Override
    public List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName) {
        Map<String, List<String>> constraintMap = new HashMap<>();

        try (ResultSet rs = metaData.getIndexInfo(null, schemaName, tableName, true, false)) {
            while (rs.next()) {
                String indexName = rs.getString("INDEX_NAME");
                String columnName = rs.getString("COLUMN_NAME");
                boolean isUnique = !rs.getBoolean("NON_UNIQUE");

                if (indexName == null || columnName == null || !isUnique) continue;

                if ("PRIMARY".equalsIgnoreCase(indexName)) continue;

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

    private void updateForeignKeys(List<ForeignKey> foreignKeys, List<Column> columns) {
        Map<String, Column> columnMap = columns.stream()
                .collect(Collectors.toMap(Column::getName, col -> col));

        for (ForeignKey fk : foreignKeys) {
            Column col = columnMap.get(fk.getFkColumn());
            if (col != null) {
                fk.setNullable(col.isNullable());
                fk.setUnique(col.isUnique());
            }
        }
    }
}
