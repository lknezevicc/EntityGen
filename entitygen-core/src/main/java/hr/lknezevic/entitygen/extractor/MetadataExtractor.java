package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.model.Table;

import java.sql.Connection;
import java.util.List;

public interface MetadataExtractor {
    List<Schema> extractSchemas(Connection connection);
    List<Table> extractTables(Connection connection, String schemaName);
    List<Column> extractColumns(Connection connection, String tableName);
}
