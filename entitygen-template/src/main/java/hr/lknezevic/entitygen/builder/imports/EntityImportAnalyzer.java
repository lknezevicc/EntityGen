package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class EntityImportAnalyzer extends AbstractImportAnalyzer {

    public EntityImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.LOMBOK_DATA,
                TemplateImport.LOMBOK_NO_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_ALL_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_BUILDER,
                TemplateImport.JPA_ENTITY,
                TemplateImport.JPA_TABLE,
                TemplateImport.JPA_COLUMN
        ));

        if (hasAdditionalImports())
            analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        super.analyzeFields();
        this.analyzeFields();
        super.analyzeRelations();
        this.analyzeRelations();
        analyzeRelationByType();
    }

    @Override
    protected boolean hasAdditionalImports() {
        return true;
    }

    @Override
    protected void analyzeFields() {
        if (entity.getFields().isEmpty()) return;

        if (!entity.getUniqueConstraints().isEmpty())
            imports.add(TemplateImport.JPA_UNIQUE_CONSTRAINT);

        if (entity.isCompositeKey()) {
            otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, entity.getEmbeddedId().getClassName()));
            imports.add(TemplateImport.JPA_EMBEDDED_ID);
        } else {
            imports.add(TemplateImport.JPA_ID);

            if (entity.getFields().stream().anyMatch(Field::isAutoIncrement)) {
                imports.add(TemplateImport.JPA_GENERATED_VALUE);
                imports.add(TemplateImport.JPA_GENERATION_TYPE);
            }
        }

        if (entity.getFields().stream().anyMatch(Field::isUnique))
            imports.add(TemplateImport.JPA_UNIQUE_CONSTRAINT);

        if (entity.getFields().stream().anyMatch(Field::isLob))
            imports.add(TemplateImport.JPA_LOB);
    }

    @Override
    protected void analyzeRelations() {
        if (entity.getRelations().isEmpty()) return;

        log.debug("Entity {} has {} relations", entity.getClassName(), entity.getRelations().size());
        entity.getRelations().forEach(relation -> {
            log.debug("Analyzing relation: {}", relation.getType());
            log.debug("Relation target entity: {}", relation.getTargetEntityClass());
            if (!relation.getJoinColumns().isEmpty())
                log.debug("Relation join columns: {}", relation.getJoinColumns());
        });

        if (entity.getRelations().stream().anyMatch(relation -> !relation.getJoinColumns().isEmpty())) {
            imports.add(TemplateImport.JPA_JOIN_COLUMN);
        }

        if (entity.getRelations().stream().anyMatch(relation -> relation.getJoinColumns().size() > 1)) {
            imports.add(TemplateImport.JPA_JOIN_COLUMNS);
        }
    }

    private void analyzeRelationByType() {
        imports.add(TemplateImport.JPA_FETCH_TYPE);
        imports.add(TemplateImport.JPA_CASCADE_TYPE);

        Set<RelationType> relationTypes = entity.getRelations().stream()
                .map(Relation::getType)
                .collect(Collectors.toSet());

        boolean hasCollection = entity.getRelations().stream()
                .anyMatch(relation -> relation.getCollectionType() != null);

        relationTypes.forEach(relationType -> {
            switch (relationType) {
                case ONE_TO_ONE -> {
                    imports.add(TemplateImport.JPA_ONE_TO_ONE);
                }

                case ONE_TO_MANY -> {
                    imports.add(TemplateImport.JPA_ONE_TO_MANY);
                    imports.add(TemplateImport.JPA_CASCADE_TYPE);
                    imports.add(TemplateImport.JAVA_ARRAY_LIST);
                }

                case MANY_TO_ONE -> {
                    imports.add(TemplateImport.JPA_MANY_TO_ONE);
                }

                case MANY_TO_MANY -> {
                    imports.add(TemplateImport.JPA_MANY_TO_MANY);
                    imports.add(TemplateImport.JPA_JOIN_TABLE);
                    imports.add(TemplateImport.JPA_INVERSE_JOIN_COLUMN);
                    imports.add(TemplateImport.JAVA_LINKED_HASH_SET);
                }
            }

            if (hasCollection && (relationType == RelationType.ONE_TO_MANY || relationType == RelationType.MANY_TO_MANY)) {
                imports.add(TemplateImport.LOMBOK_TO_STRING);
                imports.add(TemplateImport.LOMBOK_EQUALS_AND_HASH_CODE);
            }
        });

        if (entity.getRelations().stream().anyMatch(relation -> relation.getMapsId() != null))
            imports.add(TemplateImport.JPA_MAPS_ID);
    }

}
