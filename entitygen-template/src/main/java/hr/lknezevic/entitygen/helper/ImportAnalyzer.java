package hr.lknezevic.entitygen.helper;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.*;

public class ImportAnalyzer {
    private final Set<TemplateImport> imports = new HashSet<>();

    public ImportAnalyzer analyzeFor(Entity entity, ComponentType componentType) {
        if (componentType == ComponentType.REPOSITORY) {
            imports.add(TemplateImport.SPRING_REPOSITORY);
            imports.add(TemplateImport.SPRING_JPA_REPOSITORY);
            imports.add(TemplateImport.JAVA_LIST);
            return this;
        }

        if (componentType == ComponentType.SERVICE) {
            imports.add(TemplateImport.JAVA_LIST);
            return this;
        }

        if (componentType == ComponentType.SERVICE_IMPL) {
            imports.add(TemplateImport.SPRING_SERVICE);
            imports.add(TemplateImport.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR);
            imports.add(TemplateImport.JAVA_LIST);
            return this;
        }

        if (componentType == ComponentType.CONTROLLER) {
            imports.add(TemplateImport.SPRING_REST_CONTROLLER);
            imports.add(TemplateImport.SPRING_REQUEST_MAPPING);
            imports.add(TemplateImport.SPRING_GET_MAPPING);
            imports.add(TemplateImport.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR);
            imports.add(TemplateImport.JAVA_LIST);
            imports.add(TemplateImport.SPRING_RESPONSE_ENTITY);
            return this;
        }

        if (componentType == ComponentType.DTO) {
            addImportsForFields(entity.getFields());
            return this;
        }

        if (componentType == ComponentType.EMBEDDABLE) {
            imports.add(TemplateImport.LOMBOK_DATA);
            imports.add(TemplateImport.LOMBOK_ALL_ARGS_CONSTRUCTOR);
            imports.add(TemplateImport.LOMBOK_NO_ARGS_CONSTRUCTOR);
            imports.add(TemplateImport.JPA_EMBEDDABLE);
            imports.add(TemplateImport.JAVA_SERIALIZABLE);
            if (entity.getEmbeddedId() != null) {
                addImportsForFields(entity.getEmbeddedId().getFields());
            }
            return this;
        }

        analyzeForEntity(entity);

        return this;
    }

    public List<String> getSortedImports() {
        List<String> sortedImports = imports.stream()
                .sorted(Comparator
                        .comparingInt(TemplateImport::getOrder)
                        .thenComparing(TemplateImport::getValue))
                .map(TemplateImport::getValue)
                .toList();

        List<String> result = new ArrayList<>(sortedImports);
        result.add("");

        return result;
    }

    private void analyzeForEntity(Entity entity) {
        addDefaultEntityImports();

        if (entity.isCompositeKey()) {
            imports.add(TemplateImport.JPA_EMBEDDED_ID);
            imports.add(TemplateImport.LOMBOK_BUILDER_DEFAULT);
        } else {
            imports.add(TemplateImport.JPA_ID);
        }

        if (entity.getFields().stream().anyMatch(Field::isAutoIncrement)) {
            imports.add(TemplateImport.JPA_GENERATED_VALUE);
            imports.add(TemplateImport.JPA_GENERATION_TYPE);
        }

        if (!entity.getUniqueConstraints().isEmpty()) {
            imports.add(TemplateImport.JPA_UNIQUE_CONSTRAINT);
        }

        addImportsForFields(entity.getFields());

        for (Relation relation : entity.getRelations()) {
            analyzeRelationImports(relation);
        }

        // Add Hibernate OnDelete imports if entity has relations
//        if (!entity.getRelations().isEmpty()) {
//            imports.add(TemplateImport.HIBERNATE_ON_DELETE);
//            imports.add(TemplateImport.HIBERNATE_ON_DELETE_ACTION);
//        }
    }

    private void addDefaultEntityImports() {
        imports.addAll(Set.of(
                TemplateImport.LOMBOK_DATA,
                TemplateImport.LOMBOK_NO_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_ALL_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_BUILDER,
                TemplateImport.JPA_ENTITY,
                TemplateImport.JPA_TABLE
        ));
    }

    private void addImportsForFields(List<Field> fields) {
        imports.add(TemplateImport.JPA_COLUMN);
        for (Field field : fields) {
            TemplateImport typeImport = findImportForJavaType(field.getJavaType());
            if (typeImport != null) {
                imports.add(typeImport);
            }

            if (field.isLob()) {
                imports.add(TemplateImport.JPA_LOB);
            }
        }
    }

    private TemplateImport findImportForJavaType(String javaType) {
        return switch (javaType) {
            case "BigDecimal" -> TemplateImport.JAVA_BIG_DECIMAL;
            case "BigInteger" -> TemplateImport.JAVA_BIG_INTEGER;
            case "Date" -> TemplateImport.JAVA_DATE;
            case "LocalDate" -> TemplateImport.JAVA_LOCAL_DATE;
            case "LocalTime" -> TemplateImport.JAVA_LOCAL_TIME;
            case "LocalDateTime" -> TemplateImport.JAVA_LOCAL_DATE_TIME;
            case "UUID" -> TemplateImport.JAVA_UUID;
            default -> null;
        };
    }

    private void analyzeRelationImports(Relation relation) {
        switch (relation.getType()) {
            case ONE_TO_ONE -> {
                imports.add(TemplateImport.JPA_ONE_TO_ONE);
                imports.add(TemplateImport.JPA_JOIN_COLUMN);
            }
            case ONE_TO_MANY -> {
                imports.add(TemplateImport.JPA_ONE_TO_MANY);
                imports.add(TemplateImport.JPA_CASCADE_TYPE);
                imports.add(TemplateImport.JAVA_LIST);
                imports.add(TemplateImport.JAVA_ARRAY_LIST);
                imports.add(TemplateImport.JPA_FETCH_TYPE);
            }
            case MANY_TO_ONE -> {
                imports.add(TemplateImport.JPA_MANY_TO_ONE);
                imports.add(TemplateImport.JPA_JOIN_COLUMN);
                imports.add(TemplateImport.JPA_FETCH_TYPE);
            }
            case MANY_TO_MANY -> {
                imports.add(TemplateImport.JPA_MANY_TO_MANY);
                imports.add(TemplateImport.JPA_JOIN_TABLE);
                imports.add(TemplateImport.JPA_JOIN_COLUMN);
                imports.add(TemplateImport.JPA_INVERSE_JOIN_COLUMN);
                imports.add(TemplateImport.JPA_CASCADE_TYPE);
                imports.add(TemplateImport.JAVA_SET);
                imports.add(TemplateImport.JAVA_HASH_SET);
            }
            case MANY_TO_MANY_DIRECT -> {
                imports.add(TemplateImport.JPA_MANY_TO_MANY);
                imports.add(TemplateImport.JPA_JOIN_TABLE);
                imports.add(TemplateImport.JPA_CASCADE_TYPE);
                imports.add(TemplateImport.JAVA_SET);
                imports.add(TemplateImport.JAVA_HASH_SET);
            }
        }

        if (relation.getType() == RelationType.ONE_TO_MANY ||
                relation.getType() == RelationType.MANY_TO_MANY ||
                relation.getType() == RelationType.MANY_TO_MANY_DIRECT) {

            imports.add(TemplateImport.LOMBOK_TO_STRING);
            imports.add(TemplateImport.LOMBOK_EQUALS_AND_HASH_CODE);
        }
    }

}
