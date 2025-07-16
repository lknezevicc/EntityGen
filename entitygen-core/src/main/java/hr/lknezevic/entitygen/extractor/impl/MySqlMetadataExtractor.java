package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.model.Table;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class MySqlMetadataExtractor implements MetadataExtractor {
    private final SchemaFilter schemaFilter;

    @Override
    public List<Schema> extractSchemas(Connection connection) {
        List<Schema> schemas = new ArrayList<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rsSchemas = metaData.getSchemas();

            while (rsSchemas.next()) {
                String schemaName = rsSchemas.getString("TABLE_SCHEM");

                if (schemaFilter.isSystemSchema(schemaName)) continue;

                Schema tempSchema = Schema.builder()
                        .name(schemaName)
                        .build();

                schemas.add(tempSchema);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return schemas;
    }

    @Override
    public List<Table> extractTables(Connection connection, String schemaName) {
        return List.of();
    }

    @Override
    public List<Column> extractColumns(Connection connection, String tableName) {
        return List.of();
    }
}
