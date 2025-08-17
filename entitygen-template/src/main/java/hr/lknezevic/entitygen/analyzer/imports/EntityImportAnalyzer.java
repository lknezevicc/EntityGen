package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.enums.Imports;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Analyzes and provides the necessary imports for an Entity class.
 */
public class EntityImportAnalyzer extends AbstractImportAnalyzer {

    public EntityImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Entity class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                Imports.JAVA_GENERATED,
                Imports.LOMBOK_DATA,
                Imports.LOMBOK_NO_ARGS_CONSTRUCTOR,
                Imports.LOMBOK_ALL_ARGS_CONSTRUCTOR,
                Imports.LOMBOK_BUILDER,
                Imports.JPA_ENTITY,
                Imports.JPA_TABLE
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        super.analyzeFields();
        this.analyzeFields();
        this.analyzeRelations();
    }

    @Override
    protected void analyzeFields() {
        if (entity.getFields().isEmpty()) return;

        if (entity.getFields().size() - entity.getPrimaryKeyFields().size() > 0) {
            imports.add(Imports.JPA_COLUMN);
        }

        if (!entity.getUniqueConstraints().isEmpty())
            imports.add(Imports.JPA_UNIQUE_CONSTRAINT);

        if (entity.isCompositeKey()) {
            otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, entity.getEmbeddedId().getClassName()));
            imports.add(Imports.JPA_EMBEDDED_ID);
        } else {
            imports.add(Imports.JPA_ID);

            if (entity.getFields().stream().anyMatch(Field::isAutoIncrement)) {
                imports.add(Imports.JPA_GENERATED_VALUE);
                imports.add(Imports.JPA_GENERATION_TYPE);
            }
        }

        if (entity.getFields().stream().anyMatch(Field::isUnique))
            imports.add(Imports.JPA_UNIQUE_CONSTRAINT);

        if (entity.getFields().stream().anyMatch(Field::isLob))
            imports.add(Imports.JPA_LOB);
    }

    @Override
    protected void analyzeRelations() {
        if (entity.getRelations().isEmpty()) return;

        imports.add(Imports.JPA_FETCH_TYPE);
        imports.add(Imports.JPA_CASCADE_TYPE);

        entity.getRelations().forEach(relation -> {
            CollectionType collectionType = relation.getCollectionType();
            if (collectionType == null) return;

            switch (collectionType) {
                case LIST -> {
                    imports.add(Imports.JAVA_LIST);
                    imports.add(Imports.JAVA_ARRAY_LIST);
                }
                case SET -> {
                    imports.add(Imports.JAVA_SET);
                    imports.add(Imports.JAVA_HASH_SET);
                }
                case LINKED_HASH_SET -> {
                    imports.add(Imports.JAVA_SET);
                    imports.add(Imports.JAVA_LINKED_HASH_SET);
                }
            }
        });

        Set<RelationType> relationTypes = entity.getRelations().stream()
                .map(Relation::getType)
                .collect(Collectors.toSet());

        relationTypes.forEach(relationType -> {
            switch (relationType) {
                case ONE_TO_ONE -> imports.add(Imports.JPA_ONE_TO_ONE);

                case ONE_TO_MANY -> {
                    imports.add(Imports.JPA_ONE_TO_MANY);
                    imports.add(Imports.JPA_CASCADE_TYPE);
                    imports.add(Imports.JAVA_ARRAY_LIST);
                }

                case MANY_TO_ONE -> imports.add(Imports.JPA_MANY_TO_ONE);

                case MANY_TO_MANY -> {
                    imports.add(Imports.JPA_MANY_TO_MANY);
                    imports.add(Imports.JAVA_LINKED_HASH_SET);
                }
            }

            if (relationType == RelationType.ONE_TO_MANY || relationType == RelationType.MANY_TO_MANY) {
                imports.add(Imports.LOMBOK_TO_STRING);
                imports.add(Imports.LOMBOK_EQUALS_AND_HASH_CODE);
            }
        });

        if (entity.getRelations().stream().anyMatch(relation ->
                relation.getMappedBy() == null && relation.getType() == RelationType.MANY_TO_MANY)) {
            imports.add(Imports.JPA_JOIN_TABLE);
        }

        if (entity.getRelations().stream().anyMatch(relation -> !relation.getJoinColumns().isEmpty())) {
            imports.add(Imports.JPA_JOIN_COLUMN);
        }

        if (entity.getRelations().stream().anyMatch(relation -> relation.getJoinColumns().size() > 1)) {
            imports.add(Imports.JPA_JOIN_COLUMNS);
        }

        if (entity.getRelations().stream().anyMatch(relation -> relation.getMapsId() != null))
            imports.add(Imports.JPA_MAPS_ID);
    }

}
