package hr.lknezevic.entitygen.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration class for EntityGen Maven Plugin
 * Maps all properties from application.properties to typed fields
 *
 * @author Leon Knežević
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig {
    // === Package Structure ===
    private String embeddablePackage;
    private String entityPackage;
    private String dtoPackage;
    private String repositoryPackage;
    private String servicePackage;
    private String serviceImplPackage;
    private String controllerPackage;
    private String outputDirectory;

    // === Serialization Settings ===
    @Builder.Default
    private Boolean enableSerialization = true;

    @Builder.Default
    private Boolean generateSerialVersion = true;

    // === Entity Generation Settings ===
    @Builder.Default
    private Boolean enableLombok = true;

    // === Naming Conventions ===
    @Builder.Default
    private String embeddableSuffix = "Id";

    @Builder.Default
    private String entitySuffix = "";
    
    @Builder.Default
    private String dtoSuffix = "Dto";
    
    @Builder.Default
    private String repositorySuffix = "Repository";
    
    @Builder.Default
    private String serviceSuffix = "Service";

    @Builder.Default
    private String serviceImplSuffix = "ServiceImpl";
    
    @Builder.Default
    private String controllerSuffix = "Controller";

    // === Code Generation Features ===
    @Builder.Default
    private Boolean generateRepositories = true;

    @Builder.Default
    private Boolean generateServices = true;
    
    @Builder.Default
    private Boolean generateControllers = true;
    
    @Builder.Default
    private Boolean generateDtos = true;

    @Builder.Default
    private Boolean preferLongIds = true;

    // === Schema Filtering ===
    @Builder.Default
    private String includeSchemas = "public";
    
    @Builder.Default
    private String excludeSchemas = "information_schema,pg_catalog";
    
    @Builder.Default
    private String includeTables = "";
    
    @Builder.Default
    private String excludeTables = "flyway_schema_history,log_*,temp_*";

    // === Field Generation ===
    @Builder.Default
    private Boolean generateDefaultValues = true;
    
    @Builder.Default
    private Boolean generateComments = true;
    
    @Builder.Default
    private Boolean enableColumnAnnotations = true;

    // === Output Settings ===
    @Builder.Default
    private Boolean overwriteExisting = false;

    // === Documentation ===
    @Builder.Default
    private Boolean generateJavadoc = true;
    
    @Builder.Default
    private String javadocAuthor = "EntityGen Maven Plugin";
    
    @Builder.Default
    private Boolean generateReadme = false;
    
    @Builder.Default
    private Boolean generateChangelog = false;
}
