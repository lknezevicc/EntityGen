package hr.lknezevic.entitygen.config;

import hr.lknezevic.entitygen.enums.SpringProperties;
import hr.lknezevic.entitygen.exceptions.unchecked.ConfigurationLoadException;
import hr.lknezevic.entitygen.utils.LoggingUtility;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Class for loading user-defined configuration properties from application.properties file.
 */
public class UserConfigProperties {
    private static final List<SpringProperties> PROPERTIES_TO_EXCLUDE =
            List.of(SpringProperties.URL, SpringProperties.USER, SpringProperties.PASSWORD, SpringProperties.DRIVER);
    private final List<SpringProperties> userProperties;
    private final Properties properties;
    private final String basePackage;
    private final String outputDirectory;

    public UserConfigProperties(String propertiesFile, String basePackage) throws ConfigurationLoadException {
        Path propertiesPath = Path.of(propertiesFile);
        this.basePackage = basePackage;
        this.outputDirectory = generateOutputDirectory(propertiesPath);

        userProperties = Arrays.stream(SpringProperties.values())
                .filter(p -> !PROPERTIES_TO_EXCLUDE.contains(p))
                .toList();

        properties = new Properties();
        try (InputStream is = Files.newInputStream(propertiesPath)) {
            properties.load(is);
        } catch (IOException e) {
            LoggingUtility.error("Failed to load properties from file: {}", propertiesFile, e);
            throw new ConfigurationLoadException("Could not load properties from " + propertiesFile, e);
        }
    }

    public UserConfig createUserConfig() {
        UserConfig.UserConfigBuilder builder = UserConfig.builder();
        builder.entityPackage(basePackage + ".entity");
        builder.embeddablePackage(basePackage + ".embeddable");
        builder.dtoPackage(basePackage + ".dto");
        builder.servicePackage(basePackage + ".service");
        builder.serviceImplPackage(basePackage + ".service.impl");
        builder.controllerPackage(basePackage + ".controller");
        builder.repositoryPackage(basePackage + ".repository");
        builder.outputDirectory(outputDirectory);
        
        for (SpringProperties property : userProperties) {
            String propertyKey = property.getValue();
            String propertyValue = properties.getProperty(propertyKey);
            
            if (!StringUtils.isBlank(propertyValue)) {
                mapPropertyToConfig(builder, property, propertyValue.trim());
            }
        }
        
        return builder.build();
    }

    private void mapPropertyToConfig(Object builderObj, SpringProperties property, String value) {
        var builder = (UserConfig.UserConfigBuilder) builderObj;
        switch (property) {
            // Package Configuration
            case ENTITY_PACKAGE -> builder.entityPackage(value);
            case EMBEDDABLE_PACKAGE -> builder.embeddablePackage(value);
            case DTO_PACKAGE -> builder.dtoPackage(value);
            case REPOSITORY_PACKAGE -> builder.repositoryPackage(value);
            case SERVICE_PACKAGE -> builder.servicePackage(value);
            case CONTROLLER_PACKAGE -> builder.controllerPackage(value);
            case OUTPUT_DIRECTORY -> builder.outputDirectory(value);
            
            // Naming Conventions
            case ENTITY_SUFFIX -> builder.entitySuffix(value);
            case EMBEDDABLE_SUFFIX -> builder.embeddableSuffix(value);
            case DTO_SUFFIX -> builder.dtoSuffix(value);
            case REPOSITORY_SUFFIX -> builder.repositorySuffix(value);
            case SERVICE_SUFFIX -> builder.serviceSuffix(value);
            case CONTROLLER_SUFFIX -> builder.controllerSuffix(value);
            
            // Code Generation Features
            case GENERATE_ALL_COMPONENTS -> builder.generateAllComponents(Boolean.parseBoolean(value));
            case GENERATE_DEFAULT_VALUES -> builder.generateDefaultValues(Boolean.parseBoolean(value));
            case GENERATE_COMMENTS -> builder.generateComments(Boolean.parseBoolean(value));
            case OVERWRITE_EXISTING -> builder.overwriteExisting(Boolean.parseBoolean(value));
            case JAVADOC_AUTHOR -> builder.javadocAuthor(value);

            // Schema Filtering
            case TARGET_SCHEMA -> builder.targetSchema(value);
            case INCLUDE_TABLES -> builder.includeTables(extractFromProperties(value));
            
            default -> LoggingUtility.warn("Unknown property: {} with value: {}", property.getValue(), value);
        }
    }

    private String generateOutputDirectory(Path propertiesFile) {
        return propertiesFile.getParent().getParent()
                .resolve("java")
                .toString();
    }

    private List<String> extractFromProperties(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

}
