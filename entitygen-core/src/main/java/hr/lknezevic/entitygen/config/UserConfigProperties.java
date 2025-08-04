package hr.lknezevic.entitygen.config;

import hr.lknezevic.entitygen.enums.SpringProperties;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Slf4j
public class UserConfigProperties {
    private static final List<SpringProperties> PROPERTIES_TO_EXCLUDE =
            List.of(SpringProperties.URL, SpringProperties.USER, SpringProperties.PASSWORD, SpringProperties.DRIVER);
    private final List<SpringProperties> userProperties;
    private final Properties properties;
    private final String basePackage;
    private final String outputDirectory;

    public UserConfigProperties(String propertiesFile, String basePackage) throws IOException {
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
            throw new RuntimeException("Failed to load properties from " + propertiesFile, e);
        }
    }

    /**
     * Creates UserConfig by iterating through userProperties and mapping to config fields
     */
    public UserConfig createUserConfig() {
        UserConfig.UserConfigBuilder builder = UserConfig.builder();
        builder.entityPackage(basePackage + ".entity");
        builder.embeddablePackage(basePackage + ".embeddable");
        builder.servicePackage(basePackage + ".service");
        builder.serviceImplPackage(basePackage + ".service.impl");
        builder.controllerPackage(basePackage + ".controller");
        builder.repositoryPackage(basePackage + ".repository");
        builder.dtoPackage(basePackage + ".dto");
        builder.outputDirectory(outputDirectory);
        
        for (SpringProperties property : userProperties) {
            String propertyKey = property.getValue();
            String propertyValue = properties.getProperty(propertyKey);
            
            if (propertyValue != null && !propertyValue.trim().isEmpty()) {
                mapPropertyToConfig(builder, property, propertyValue.trim());
            }
        }
        
        return builder.build();
    }
    
    /**
     * Maps SpringProperties enum to UserConfig builder methods
     */
    private void mapPropertyToConfig(UserConfig.UserConfigBuilder builder, SpringProperties property, String value) {
        switch (property) {
            // Package Structure
            case ENTITY_PACKAGE -> builder.entityPackage(value);
            case DTO_PACKAGE -> builder.dtoPackage(value);
            case REPOSITORY_PACKAGE -> builder.repositoryPackage(value);
            case SERVICE_PACKAGE -> builder.servicePackage(value);
            case CONTROLLER_PACKAGE -> builder.controllerPackage(value);
            
            // Serialization
            case ENABLE_SERIALIZATION -> builder.enableSerialization(Boolean.parseBoolean(value));
            case GENERATE_SERIAL_VERSION -> builder.generateSerialVersion(Boolean.parseBoolean(value));
            
            // Entity Generation
            case ENABLE_LOMBOK -> builder.enableLombok(Boolean.parseBoolean(value));
            
            // Naming Conventions
            case ENTITY_SUFFIX -> builder.entitySuffix(value);
            case DTO_SUFFIX -> builder.dtoSuffix(value);
            case REPOSITORY_SUFFIX -> builder.repositorySuffix(value);
            case SERVICE_SUFFIX -> builder.serviceSuffix(value);
            case CONTROLLER_SUFFIX -> builder.controllerSuffix(value);
            
            // Code Generation Features
            case GENERATE_REPOSITORIES -> builder.generateRepositories(Boolean.parseBoolean(value));
            case GENERATE_SERVICES -> builder.generateServices(Boolean.parseBoolean(value));
            case GENERATE_CONTROLLERS -> builder.generateControllers(Boolean.parseBoolean(value));
            case GENERATE_DTOS -> builder.generateDtos(Boolean.parseBoolean(value));
            
            // Schema Filtering
            case INCLUDE_SCHEMAS -> builder.includeSchemas(value);
            case EXCLUDE_SCHEMAS -> builder.excludeSchemas(value);
            case INCLUDE_TABLES -> builder.includeTables(value);
            case EXCLUDE_TABLES -> builder.excludeTables(value);
            
            // Field Generation
            case GENERATE_DEFAULT_VALUES -> builder.generateDefaultValues(Boolean.parseBoolean(value));
            case GENERATE_COMMENTS -> builder.generateComments(Boolean.parseBoolean(value));
            case ENABLE_COLUMN_ANNOTATIONS -> builder.enableColumnAnnotations(Boolean.parseBoolean(value));
            
            // Output Settings
            case OUTPUT_DIRECTORY -> builder.outputDirectory(value);
            case OVERWRITE_EXISTING -> builder.overwriteExisting(Boolean.parseBoolean(value));
            
            // Documentation
            case GENERATE_JAVADOC -> builder.generateJavadoc(Boolean.parseBoolean(value));
            case JAVADOC_AUTHOR -> builder.javadocAuthor(value);
            case GENERATE_README -> builder.generateReadme(Boolean.parseBoolean(value));
            case GENERATE_CHANGELOG -> builder.generateChangelog(Boolean.parseBoolean(value));
            
            default -> log.error("Invalid property value for property [{}]", property);
        }
    }

    private String generateOutputDirectory(Path propertiesFile) {
        return propertiesFile.getParent().getParent()
                .resolve("java")
                .toString();
    }

}
