package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.model.Table;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.List;

public class MySqlMetadataExtractor implements MetadataExtractor {

    @Override
    public List<Schema> extractSchemas(Connection connection) {
        List<Schema> schemas = List.of();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet rsSchemas = metaData.getSchemas();

            while (rsSchemas.next()) {
                String schemaName = rsSchemas.getString("TABLE_SCHEM");
                String catalog = rsSchemas.getString("TABLE_CATALOG");
                Schema tempSchema = new Schema(schemaName, catalog);
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
