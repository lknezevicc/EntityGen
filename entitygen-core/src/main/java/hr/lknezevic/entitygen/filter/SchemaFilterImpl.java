package hr.lknezevic.entitygen.filter;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.exceptions.unchecked.ConfigurationLoadException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of SchemaFilter that filters schemas and tables based on user configuration.
 * It loads system schemas from a properties file and checks if a schema is a system schema.
 */
public class SchemaFilterImpl implements SchemaFilter {
    private final Set<String> systemSchemas = new HashSet<>();
    private final String targetSchema;
    private final List<String> includedTables = new ArrayList<>();

    public SchemaFilterImpl(UserConfig userConfig, String databaseType) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("system-schemas.properties"));
            if (properties.isEmpty()) {
                throw new ConfigurationLoadException("System schemas properties file is empty or not found");
            }

            String schemas = properties.getProperty(databaseType.toLowerCase());
            if (!StringUtils.isEmpty(schemas)) {
                systemSchemas.addAll(
                        Arrays.stream(schemas.split(","))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .collect(Collectors.toSet())
                );
            }

            targetSchema = userConfig.getTargetSchema();

            if (!userConfig.getIncludeTables().isEmpty())
                includedTables.addAll(userConfig.getIncludeTables());

        } catch (Exception e) {
            throw new ConfigurationLoadException("Failed to load system schemas from properties file", e);
        }
    }

    /**
     * Checks if the given schema name is a system schema.
     *
     * @param schemaName the name of the schema to check
     * @return true if the schema is a system schema, false otherwise
     */
    @Override
    public boolean isSystemSchema(String schemaName) {
        return !StringUtils.isEmpty(schemaName) && systemSchemas.contains(schemaName.toLowerCase());
    }

    /**
     * Checks if the given schema name is the target schema.
     *
     * @param schemaName the name of the schema to check
     * @return true if the schema is the target schema, false otherwise
     */
    @Override
    public boolean isTargetSchema(String schemaName) {
        if (StringUtils.isBlank(targetSchema)) return true;
        return StringUtils.isNotBlank(schemaName) && targetSchema.equalsIgnoreCase(schemaName);
    }

    /**
     * Checks if the given table name is included in the list of tables to be processed.
     *
     * @param tableName the name of the table to check
     * @return true if the table is included, false otherwise
     */
    @Override
    public boolean isTableIncluded(String tableName) {
        return !StringUtils.isEmpty(tableName) && (includedTables.isEmpty() || includedTables.contains(tableName));
    }
}
