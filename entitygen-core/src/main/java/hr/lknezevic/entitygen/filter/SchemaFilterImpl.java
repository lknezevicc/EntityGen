package hr.lknezevic.entitygen.filter;

import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

public class SchemaFilterImpl implements SchemaFilter {
    private final Set<String> systemSchemas;

    public SchemaFilterImpl(String databaseType) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("system-schemas.properties"));
            if (properties.isEmpty()) {
                throw new RuntimeException("System schemas properties file is empty or not found");
            }

            String schemas = properties.getProperty(databaseType.toLowerCase());
            if (schemas == null || schemas.isEmpty()) {
                systemSchemas = Set.of();
            } else {
                systemSchemas = Arrays.stream(schemas.split(","))
                        .map(String::trim)
                        .map(String::toLowerCase)
                        .collect(Collectors.toSet());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to load system schemas from properties file", e);
        }
    }

    @Override
    public boolean isSystemSchema(String schemaName) {
        return schemaName != null && systemSchemas.contains(schemaName.toLowerCase());
    }
}
