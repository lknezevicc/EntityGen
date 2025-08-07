package hr.lknezevic.entitygen.model.template.modules.strategies.relation;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.model.template.modules.strategies.RelationRenderingStrategy;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering DTO relations in templates.
 */
public class DtoRelationRendering implements RelationRenderingStrategy {

    /**
     * Renders a relation for a DTO (Data Transfer Object) using a predefined template.
     *
     * @param sourceEntity the source entity of the relation
     * @param relation the relation to be rendered
     * @param targetEntity the target entity of the relation
     * @return a formatted string representing the relation in the DTO
     */
    @Override
    public String render(Entity sourceEntity, Relation relation, Entity targetEntity) {
        RelationType relationType = relation.getType();
        String fieldName = relation.getFieldName();
        String targetIdType = getTargetIdType(targetEntity);

        if (relationType == RelationType.ONE_TO_ONE || relationType == RelationType.MANY_TO_ONE) {
            return TemplateFactory.builder()
                    .template(TemplateConst.RELATION_FIELD_SINGLE)
                    .build()
                    .addParams(targetIdType, fieldName)
                    .format();
        } else {
            return TemplateFactory.builder()
                    .template(TemplateConst.RELATION_FIELD_COLLECTION)
                    .build()
                    .addParams(CollectionType.LIST.getValue(), targetIdType, fieldName)
                    .format();
        }
    }

    private String getTargetIdType(Entity targetEntity) {
        return TemplateUtil.getEntityIdType(targetEntity);
    }

}
