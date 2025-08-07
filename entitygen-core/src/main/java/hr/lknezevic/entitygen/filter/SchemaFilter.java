package hr.lknezevic.entitygen.filter;

/**
 * Interface for filtering database schemas and tables.
 */
public interface SchemaFilter {
    boolean isSystemSchema(String schemaName);
    boolean isTargetSchema(String schemaName);
    boolean isTableIncluded(String tableName);
}
