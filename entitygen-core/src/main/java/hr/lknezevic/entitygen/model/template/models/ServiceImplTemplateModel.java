package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceImplTemplateModel extends TemplateModel{

    public ServiceImplTemplateModel(ComponentType componentType, Entity entity, UserConfig config,
                                    List<String> imports, Map<String, Entity> entityByClassName) {
        super(componentType, entity, config, imports, entityByClassName);
    }

    public String getServiceName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.SERVICE);
    }

    public String getRepositoryName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.REPOSITORY);
    }

    public String getDtoName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.DTO);
    }

    public String getEntityName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.ENTITY);
    }

    public String getDtoConstructorParams() {
        List<String> params = new ArrayList<>();
        params.add(getPrimaryKeyConstructorParam());
        params.addAll(getFieldsConstructorParams());
        params.addAll(getRelationConstructorParams());
        
        return String.join(",\n            ", params);
    }

    @Override
    public List<String> getAllImports() {
        List<String> allImports = new ArrayList<>(imports);
        allImports.addAll(addAdditionalImports());

        return allImports;
    }

    private List<String> addAdditionalImports() {
        List<String> imports = new ArrayList<>();
        imports.add(config.getServicePackage() + "." + getServiceName());
        imports.add(config.getDtoPackage() + "." + getDtoName());
        imports.add(config.getEntityPackage() + "." + getEntityName());
        imports.add(config.getRepositoryPackage() + "." + getRepositoryName());
        imports.sort(String::compareTo);

        return imports;
    }

    private String getPrimaryKeyConstructorParam() {
        if (entity.isCompositeKey() && entity.getEmbeddedId() != null) {
            return String.format("entity.get%s()",
                    capitalize(entity.getEmbeddedId().getClassName()));
        } else {
            // since it's not EmbeddedId, it should have just one primary key
            return String.format("entity.get%s()",
                    capitalize(entity.getPrimaryKeyFields().getFirst().getName()));
        }
    }

    private List<String> getFieldsConstructorParams() {
        return entity.getFields().stream()
                .filter(field -> !field.isPrimaryKey())
                .map(field -> String.format("entity.get%s()", capitalize(field.getName())))
                .toList();
    }

    private List<String> getRelationConstructorParams() {
        List<String> params = new ArrayList<>();

        if (entity.getRelations().isEmpty()) return params;

        for (Relation relation : entity.getRelations()) {
            String targetIdFieldName = getTargetEntityIdField(relation);
            String relationFieldName = capitalize(relation.getFieldName());
            CollectionType collectionType = relation.getCollectionType();

            String param;
            if (relation.getType() == RelationType.ONE_TO_ONE || relation.getType() == RelationType.MANY_TO_ONE) {
                param = String.format("entity.get%s() != null ? entity.get%s().get%s() : null",
                        relationFieldName, relationFieldName, targetIdFieldName);
            } else {
                param = String.format("entity.get%s() != null ? " +
                                "entity.get%s().stream()" +
                                ".map(item -> item.get%s())" +
                                ".%s : %s",
                        relationFieldName,
                        relationFieldName,
                        targetIdFieldName,
                        collectionType == CollectionType.SET ? "collect(Collectors.toSet())" : "collect(Collectors.toList())",
                        collectionType == CollectionType.SET ? "new HashSet<>()" : "new ArrayList<>()");
            }
            params.add(param);
        }

        return params;
    }

    private String getTargetEntityIdField(Relation relation) {
        String targetId = "Id";
        Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());
        if (targetEntity == null) return targetId;

        if (targetEntity.isCompositeKey() && targetEntity.getEmbeddedId() != null) {
            targetId = capitalize(targetEntity.getEmbeddedId().getClassName());
        } else {
            targetId = capitalize(targetEntity.getPrimaryKeyFields().getFirst().getName());
        }

        return targetId;
    }

}
