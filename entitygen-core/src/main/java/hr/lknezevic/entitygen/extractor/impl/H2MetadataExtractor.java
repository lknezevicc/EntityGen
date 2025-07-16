package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.model.Table;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.util.List;

@RequiredArgsConstructor
public class H2MetadataExtractor implements MetadataExtractor {
    private final SchemaFilter schemaFilter;

    @Override
    public List<Schema> extractSchemas(Connection connection) {
        return List.of();
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
