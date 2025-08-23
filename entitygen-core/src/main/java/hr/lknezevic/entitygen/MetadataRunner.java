package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.exceptions.unchecked.MetadataExtractionException;
import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.extractor.MetadataExtractorFactory;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.providers.ConnectionProvider;
import hr.lknezevic.entitygen.providers.PropertiesConnectionProvider;
import hr.lknezevic.entitygen.providers.YamlConnectionProvider;
import hr.lknezevic.entitygen.utils.LoggingUtility;

import java.sql.Connection;
import java.util.List;

/**
 * Main runner class for the EntityGen application.
 * This class initializes the connection provider based on the user configuration and performs metadata extraction.
 * It supports both properties and YAML configuration files.
 */
public class MetadataRunner {
    private final UserConfig userConfig;
    private final ConnectionProvider connectionProvider;

    public MetadataRunner(UserConfig userConfig, String springConfigPath, String activeProfile) {
        this.userConfig = userConfig;
        connectionProvider = createConnectionProvider(resolveConfigPath(springConfigPath, activeProfile));
    }

    /**
     * Generates metadata by extracting schemas from the connected database.
     *
     * @return a list of schemas extracted from the database.
     */
    public List<Schema> generate() {
        LoggingUtility.info("Starting metadata extraction...");

        try (Connection connection = connectionProvider.getConnection()) {
            String databaseProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            LoggingUtility.info("Database product name: {}", databaseProductName);

            MetadataExtractor metadataExtractor = MetadataExtractorFactory.getMetadataExtractor(userConfig, databaseProductName);

            return metadataExtractor.extractSchemas(connection);
        } catch (Exception e) {
            throw new MetadataExtractionException("Failed to extract metadata", e);
        }
    }

    private String resolveConfigPath(String path, String profile) {
        if (!profile.equalsIgnoreCase("default")) {
            return path.replace("application.", "application-" + profile + ".");
        }

        return path;
    }

    private ConnectionProvider createConnectionProvider(String path) {
        if (path.endsWith(".properties")) {
            LoggingUtility.debug("Creating PropertiesConnectionProvider...");
            return new PropertiesConnectionProvider(path);
        } else if (path.endsWith(".yaml") || path.endsWith(".yml")) {
            LoggingUtility.debug("Creating YamlConnectionProvider...");
            return new YamlConnectionProvider(path);
        } else {
            throw new IllegalArgumentException("Unsupported config file: " + path);
        }
    }
}