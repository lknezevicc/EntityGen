package hr.lknezevic.entitygen.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class for EntityGen Maven Plugin
 * Maps all properties from application.properties to typed fields
 *
 * @author leonknezevic
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserConfig {
    // === Package Configuration ===
    private String embeddablePackage;
    private String entityPackage;
    private String dtoPackage;
    private String repositoryPackage;
    private String servicePackage;
    private String serviceImplPackage;
    private String controllerPackage;
    private String outputDirectory;

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
    private Boolean generateAllComponents = true;

    @Builder.Default
    private Boolean generateDefaultValues = false;

    @Builder.Default
    private Boolean generateComments = true;

    @Builder.Default
    private Boolean overwriteExisting = true;

    @Builder.Default
    private String javadocAuthor = "EntityGen Maven Plugin";

    // === Schema filtering ===
    @Builder.Default
    private String targetSchema = "";

    @Builder.Default
    private List<String> includeTables = new ArrayList<>();

    public static UserConfig defaultConfig() {
        return UserConfig.builder().build();
    }
}
