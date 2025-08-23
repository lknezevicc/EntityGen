package hr.lknezevic.entitygen.rendering.strategies.relation;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.model.domain.Relation;
import hr.lknezevic.entitygen.rendering.strategies.RelationRenderingStrategy;
import hr.lknezevic.entitygen.utils.RelationRenderingUtil;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for rendering entity relations in templates.
 */
public class EntityRelationRendering implements RelationRenderingStrategy {

    /**
     * Renders a relation for an entity using a predefined template.
     *
     * @param sourceEntity the source entity of the relation
     * @param relation the relation to be rendered
     * @param targetEntity the target entity of the relation
     * @return a formatted string representing the relation in the entity
     */
    @Override
    public String render(Entity sourceEntity, Relation relation, Entity targetEntity) {
        return switch (relation.getType()) {
            case MANY_TO_ONE -> renderManyToOne(sourceEntity, relation, targetEntity);
            case ONE_TO_ONE -> renderOneToOne(sourceEntity, relation, targetEntity);
            case ONE_TO_MANY -> renderOneToMany(relation, targetEntity);
            case MANY_TO_MANY -> renderManyToMany(relation, targetEntity);
        };
    }

    private String renderManyToOne(Entity sourceEntity, Relation relation, Entity targetEntity) {
        String comment = buildRelationComment(relation.getType().name(), targetEntity.getClassName());

        String manyToOneParams = RelationRenderingUtil.buildManyToOneParams(relation);
        String joinColumnAnnotations = buildJoinColumnAnnotations(sourceEntity, relation, targetEntity);
        
        return comment + TemplateConst.NEW_LINE +
                TemplateFactory.builder()
                        .template(TemplateConst.MANY_TO_ONE_RELATION)
                        .build()
                        .addParam(manyToOneParams)
                        .addParam(joinColumnAnnotations + TemplateConst.NEW_LINE)
                        .addParam(targetEntity.getClassName())
                        .addParam(relation.getFieldName())
                        .format();
    }

    private String renderOneToOne(Entity sourceEntity, Relation relation, Entity targetEntity) {
        String comment = buildRelationComment(relation.getType().name(), targetEntity.getClassName());
        
        String oneToOneParams = RelationRenderingUtil.buildOneToOneParams(relation);
        String annotations = buildOneToOneAnnotations(sourceEntity, relation, targetEntity);
        
        return comment + TemplateConst.NEW_LINE +
                TemplateFactory.builder()
                        .template(TemplateConst.ONE_TO_ONE_RELATION)
                        .build()
                        .addParam(oneToOneParams)
                        .addParam(annotations + (annotations.isEmpty() ? "" : TemplateConst.NEW_LINE))
                        .addParam(targetEntity.getClassName())
                        .addParam(relation.getFieldName())
                        .format();
    }

    private String renderOneToMany(Relation relation, Entity targetEntity) {
        String comment = buildRelationComment(relation.getType().name(), targetEntity.getClassName());
        
        String oneToManyParams = RelationRenderingUtil.buildOneToManyParams(relation);
        String collectionType = relation.getCollectionType().getValue();
        String collectionInit = TemplateConst.getCollectionImplementation(relation.getCollectionType());
        
        return comment + TemplateConst.NEW_LINE +
                TemplateFactory.builder()
                        .template(TemplateConst.ONE_TO_MANY_RELATION)
                        .build()
                        .addParam(oneToManyParams)
                        .addParam(collectionType)
                        .addParam(targetEntity.getClassName())
                        .addParam(relation.getFieldName())
                        .addParam(collectionInit)
                        .format();
    }

    private String renderManyToMany(Relation relation, Entity targetEntity) {
        String comment = buildRelationComment(relation.getType().name(), targetEntity.getClassName());
        String collectionType = (relation.getCollectionType() == CollectionType.LIST) ?
                CollectionType.LIST.getValue() :
                CollectionType.SET.getValue();
        String collectionInit = TemplateConst.getCollectionImplementation(relation.getCollectionType());

        if (relation.getMappedBy() != null) {
            return comment + TemplateConst.NEW_LINE +
                    TemplateFactory.builder()
                            .template(TemplateConst.MANY_TO_MANY_MAPPED_BY_RELATION)
                            .build()
                            .addParam(relation.getMappedBy())
                            .addParams(collectionType, targetEntity.getClassName(), relation.getFieldName())
                            .addParams(collectionInit)
                            .format();
        }

        String manyToManyParams = RelationRenderingUtil.buildManyToManyParams(relation);
        String joinTableAnnotations = relation.getMappedBy() == null ? buildJoinTableAnnotations(relation) : "";

        
        return comment + TemplateConst.NEW_LINE +
                TemplateFactory.builder()
                        .template(TemplateConst.MANY_TO_MANY_RELATION)
                        .build()
                        .addParam(manyToManyParams)
                        .addParam(joinTableAnnotations + (joinTableAnnotations.isEmpty() ? "" : TemplateConst.NEW_LINE))
                        .addParam(collectionType)
                        .addParam(targetEntity.getClassName())
                        .addParam(relation.getFieldName())
                        .addParam(collectionInit)
                        .format();
    }

    private String buildRelationComment(String relationType, String targetClass) {
        return TemplateFactory.builder()
                .template(TemplateConst.RELATION_COMMENT)
                .build()
                .addParams(relationType, targetClass)
                .format();
    }

    private String buildJoinColumnAnnotations(Entity sourceEntity, Relation relation, Entity targetEntity) {
        List<String> additionalParams = new ArrayList<>();

        if (relation.getOptional() != null && !relation.getOptional()) {
            additionalParams.add(TemplateFactory.builder()
                    .template(TemplateConst.COLUMN_PARAM_NULLABLE)
                    .build()
                    .addParam(false)
                    .format()
            );
        }

        if (relation.getType() == RelationType.MANY_TO_ONE) {
            return TemplateUtil.buildJoinColumns(
                    relation.getJoinColumns(),
                    relation.getReferencedColumns(),
                    sourceEntity.isCompositeKey() || targetEntity.isCompositeKey(),
                    additionalParams
            );
        } else {
            return TemplateUtil.buildJoinColumns(
                    relation.getJoinColumns(),
                    relation.getReferencedColumns(),
                    false,
                    additionalParams
            );
        }
    }

    private String buildJoinTableAnnotations(Relation relation) {
        return TemplateUtil.buildJoinTable(relation);
    }

    private String buildOneToOneAnnotations(Entity sourceEntity, Relation relation, Entity targetEntity) {
        List<String> annotations = new ArrayList<>();

        if (relation.getMapsId() != null) {
            annotations.add(TemplateUtil.buildMapsId(relation));
        }

        String joinColumnAnnotations = buildJoinColumnAnnotations(sourceEntity, relation, targetEntity);
        if (!joinColumnAnnotations.isEmpty()) {
            annotations.add(joinColumnAnnotations);
        }

        return TemplateUtil.joinParams(TemplateConst.NEW_LINE, annotations.toArray(new String[0]));
    }
}
