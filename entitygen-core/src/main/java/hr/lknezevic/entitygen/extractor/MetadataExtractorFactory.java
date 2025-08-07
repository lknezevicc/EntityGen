package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.extractor.impl.H2MetadataExtractor;
import hr.lknezevic.entitygen.extractor.impl.MySqlMetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.filter.SchemaFilterImpl;

/**
 * Factory class for creating instances of MetadataExtractor based on the database type.
 */
public class MetadataExtractorFactory {

    /**
     * Creates a MetadataExtractor based on the provided database product name.
     * @param userConfig user configuration for schema filtering
     * @param databaseProductName name of the database
     *
     * @return an instance of MetadataExtractor
     */
    public static MetadataExtractor getMetadataExtractor(UserConfig userConfig, String databaseProductName) {
        SchemaFilter schemaFilter = new SchemaFilterImpl(userConfig, databaseProductName);

        return switch (databaseProductName.toLowerCase()) {
            case "mysql" -> new MySqlMetadataExtractor(schemaFilter);
            case "h2" -> new H2MetadataExtractor(schemaFilter);
            default -> throw new IllegalArgumentException("Unsupported database: " + databaseProductName);
        };
    }

}
