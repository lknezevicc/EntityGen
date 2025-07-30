package hr.lknezevic.entitygen.enums;

import lombok.Getter;

@Getter
public enum SpringProperties {
    // === Database Connection ===
    URL("spring.datasource.url"),
    USER("spring.datasource.username"),
    PASSWORD("spring.datasource.password"),
    DRIVER("spring.datasource.driver-class-name"),
    
    // === Package Structure ===
    ENTITY_PACKAGE("entitygen.package.entity"),
    DTO_PACKAGE("entitygen.package.dto"),
    REPOSITORY_PACKAGE("entitygen.package.repository"),
    SERVICE_PACKAGE("entitygen.package.service"),
    CONTROLLER_PACKAGE("entitygen.package.controller"),
    
    // === Serialization Settings ===
    ENABLE_SERIALIZATION("entitygen.serialization.enable"),
    GENERATE_SERIAL_VERSION("entitygen.serialization.generate-version"),
    
    // === Entity Generation Settings ===
    ENABLE_LOMBOK("entitygen.entity.lombok.enable"),
    
    // === Naming Conventions ===
    ENTITY_SUFFIX("entitygen.naming.entity.suffix"), // Entity, Model, ""
    DTO_SUFFIX("entitygen.naming.dto.suffix"), // Dto, Response, Request
    REPOSITORY_SUFFIX("entitygen.naming.repository.suffix"), // Repository, Dao
    SERVICE_SUFFIX("entitygen.naming.service.suffix"), // Service, Manager
    CONTROLLER_SUFFIX("entitygen.naming.controller.suffix"), // Controller, Resource
    
    // === Code Generation Features ===
    GENERATE_REPOSITORIES("entitygen.generate.repositories"),
    GENERATE_SERVICES("entitygen.generate.services"),
    GENERATE_CONTROLLERS("entitygen.generate.controllers"),
    GENERATE_DTOS("entitygen.generate.dtos"),
    GENERATE_MAPPERS("entitygen.generate.mapstruct-mappers"),
    
    // === Schema Filtering ===
    INCLUDE_SCHEMAS("entitygen.schema.include"), // schema1,schema2
    EXCLUDE_SCHEMAS("entitygen.schema.exclude"),
    INCLUDE_TABLES("entitygen.table.include"), // users,orders,products
    EXCLUDE_TABLES("entitygen.table.exclude"), // logs,temp_*
    
    // === Field Generation ===
    GENERATE_DEFAULT_VALUES("entitygen.field.default-values.enable"),
    GENERATE_COMMENTS("entitygen.field.comments.enable"),
    ENABLE_COLUMN_ANNOTATIONS("entitygen.field.column-annotations.enable"),
    
    // === Output Settings ===
    OUTPUT_DIRECTORY("entitygen.output.directory"),
    OVERWRITE_EXISTING("entitygen.output.overwrite"),
    
    // === Documentation ===
    GENERATE_JAVADOC("entitygen.documentation.javadoc.enable"),
    JAVADOC_AUTHOR("entitygen.documentation.javadoc.author"),
    GENERATE_README("entitygen.documentation.readme.enable"),
    GENERATE_CHANGELOG("entitygen.documentation.changelog.enable");

    private final String value;

    SpringProperties(String value) {
        this.value = value;
    }
}
