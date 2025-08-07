package hr.lknezevic.entitygen.enums;

import lombok.Getter;

/**
 * Enum representing various Spring properties used in the application.
 */
@Getter
public enum SpringProperties {
    // === Database Connection ===
    URL("spring.datasource.url"),
    USER("spring.datasource.username"),
    PASSWORD("spring.datasource.password"),
    DRIVER("spring.datasource.driver-class-name"),
    
    // === Package Structure ===
    EMBEDDABLE_PACKAGE("entitygen.package.embeddable"),
    ENTITY_PACKAGE("entitygen.package.entity"),
    DTO_PACKAGE("entitygen.package.dto"),
    REPOSITORY_PACKAGE("entitygen.package.repository"),
    SERVICE_PACKAGE("entitygen.package.service"),
    CONTROLLER_PACKAGE("entitygen.package.controller"),
    OUTPUT_DIRECTORY("entitygen.output.directory"),
    
    // === Naming Conventions ===
    ENTITY_SUFFIX("entitygen.naming.entity.suffix"),
    EMBEDDABLE_SUFFIX("entitygen.naming.embeddable.suffix"),
    DTO_SUFFIX("entitygen.naming.dto.suffix"),
    REPOSITORY_SUFFIX("entitygen.naming.repository.suffix"),
    SERVICE_SUFFIX("entitygen.naming.service.suffix"),
    CONTROLLER_SUFFIX("entitygen.naming.controller.suffix"),
    
    // === Code Generation Features ===
    GENERATE_ALL_COMPONENTS("entitygen.features.generate-all-components"),
    GENERATE_DEFAULT_VALUES("entitygen.features.generate-default-values"),
    GENERATE_COMMENTS("entitygen.features.generate-comments"),
    OVERWRITE_EXISTING("entitygen.features.overwrite-existing"),
    JAVADOC_AUTHOR("entitygen.features.javadoc.author"),
    
    // === Schema Filtering ===
    TARGET_SCHEMA("entitygen.schema.target"),
    INCLUDE_TABLES("entitygen.table.include");

    private final String value;

    SpringProperties(String value) {
        this.value = value;
    }
}
