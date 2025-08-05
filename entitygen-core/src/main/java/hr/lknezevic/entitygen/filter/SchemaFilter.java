package hr.lknezevic.entitygen.filter;

public interface SchemaFilter {
    boolean isSystemSchema(String schemaName);
    boolean isTargetSchema(String schemaName);
    boolean isTableIncluded(String tableName);
}
