package hr.lknezevic.entitygen.extractor.impl;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.model.*;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.List;

@RequiredArgsConstructor
public class H2MetadataExtractor implements MetadataExtractor {
    private final SchemaFilter schemaFilter;

    @Override
    public List<Schema> extractSchemas(Connection connection) {
        return List.of();
    }

    @Override
    public List<Table> extractTables(DatabaseMetaData metaData, String schemaName) {
        return List.of();
    }

    @Override
    public List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName) {
        return List.of();
    }

    @Override
    public List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName) {
        return List.of();
    }

    @Override
    public List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName) {
        return List.of();
    }
}
