package hr.lknezevic.entitygen.filter;

import hr.lknezevic.entitygen.config.UserConfig;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SchemaFilterImpl implements SchemaFilter {
    private final Set<String> systemSchemas = new HashSet<>();
    private final String targetSchema;
    private final List<String> includedTables = new ArrayList<>();

    public SchemaFilterImpl(UserConfig userConfig, String databaseType) {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("system-schemas.properties"));
            if (properties.isEmpty()) {
                throw new RuntimeException("System schemas properties file is empty or not found");
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

            if (!StringUtils.isEmpty(userConfig.getTargetSchema())) {
                throw new RuntimeException("Target schema is not defined in user configuration");
            }

            if (!userConfig.getIncludeTables().isEmpty())
                includedTables.addAll(userConfig.getIncludeTables());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load system schemas from properties file", e);
        }
    }

    @Override
    public boolean isSystemSchema(String schemaName) {
        return !StringUtils.isEmpty(schemaName) && systemSchemas.contains(schemaName.toLowerCase());
    }

    @Override
    public boolean isTargetSchema(String schemaName) {
        if (StringUtils.isBlank(targetSchema)) return true;
        return StringUtils.isNotBlank(schemaName) && targetSchema.equalsIgnoreCase(schemaName);
    }

    @Override
    public boolean isTableIncluded(String tableName) {
        return !StringUtils.isEmpty(tableName) && (includedTables.isEmpty() || includedTables.contains(tableName));
    }
}
