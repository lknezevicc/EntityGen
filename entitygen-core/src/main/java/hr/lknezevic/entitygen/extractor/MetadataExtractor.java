package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.model.*;

import java.sql.*;
import java.util.List;

/**
 * Interface for extracting metadata from a database.
 * This interface defines methods to extract schemas, tables, columns, foreign keys, and unique constraints.
 *
 * @author leonknezevic
 */
public interface MetadataExtractor {
    List<Schema> extractSchemas(Connection connection);
    List<Table> extractTables(DatabaseMetaData metaData, String schemaName);
    List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName);
    List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName);
    List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName);
}
