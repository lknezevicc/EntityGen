package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.extractor.MetadataExtractor;
import hr.lknezevic.entitygen.extractor.MetadataExtractorFactory;
import hr.lknezevic.entitygen.model.Schema;
import hr.lknezevic.entitygen.providers.ConnectionProvider;
import hr.lknezevic.entitygen.providers.PropertiesConnectionProvider;
import hr.lknezevic.entitygen.providers.YamlConnectionProvider;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;

@Slf4j
public class EntityGenRunner {
    private final ConnectionProvider connectionProvider;

    public EntityGenRunner(String springConfigPath, String activeProfile) {
        String resolvedConfigPath = resolveConfigPath(springConfigPath, activeProfile);
        log.info("Using config path: {}", resolvedConfigPath);
        this.connectionProvider = createConnectionProvider(resolvedConfigPath);
    }

    public List<Schema> generate() {
        log.info("Starting metadata extraction...");

        try (Connection connection = connectionProvider.getConnection()) {
            String databaseProductName = connection.getMetaData().getDatabaseProductName().toLowerCase();
            log.info("Connected to database: {}", databaseProductName);

            MetadataExtractor metadataExtractor = new MetadataExtractorFactory().getMetadataExtractor(databaseProductName);

            List<Schema> schemas = metadataExtractor.extractSchemas(connection);
            log.info("Extraction completed. Found {} schemas.", schemas.size());

            return schemas;
        } catch (Exception e) {
            throw new RuntimeException("Error during metadata extraction", e);
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
            log.debug("Creating PropertiesConnectionProvider...");
            return new PropertiesConnectionProvider(path);
        } else if (path.endsWith(".yaml") || path.endsWith(".yml")) {
            log.debug("Creating YamlConnectionProvider...");
            return new YamlConnectionProvider(path);
        } else {
            throw new IllegalArgumentException("Unsupported config file: " + path);
        }
    }
}