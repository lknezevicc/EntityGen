package hr.lknezevic.entitygen.model.template.modules.strategies.relation;

import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.model.template.modules.strategies.RelationRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering service implementation relations.
 */
public class ServiceImplRelationRendering implements RelationRenderingStrategy {

    /**
     * Renders a relation for a service implementation using a predefined template.
     *
     * @param sourceEntity the source entity of the relation
     * @param relation the relation to be rendered
     * @param targetEntity the target entity of the relation
     * @return a formatted string representing the relation in the service implementation
     */
    @Override
    public String render(Entity sourceEntity, Relation relation, Entity targetEntity) {
        String targetIdFieldName = NamingUtil.capitalize(getTargetIdFieldName(targetEntity));
        String relationFieldName = NamingUtil.capitalize(relation.getFieldName());

        if (relation.getType() == RelationType.ONE_TO_ONE || relation.getType() == RelationType.MANY_TO_ONE) {
            return TemplateFactory.builder()
                    .template(TemplateConst.SERVICE_PK_LAMBDA_SIMPLE)
                    .build()
                    .addParams(relationFieldName, relationFieldName, targetIdFieldName)
                    .format();
        } else {
            return TemplateFactory.builder()
                    .template(TemplateConst.SERVICE_RELATION_LIST)
                    .build()
                    .addParams(relationFieldName, relationFieldName, targetIdFieldName)
                    .format();
        }
    }

    private String getTargetIdFieldName(Entity targetEntity) {
         return TemplateUtil.getEntityIdFieldName(targetEntity);
    }

}
