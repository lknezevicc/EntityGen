package hr.lknezevic.entitygen.model.template.modules.strategies.relation;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.model.template.modules.strategies.RelationRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.utils.TemplateUtil;

public class ServiceImplRelationRendering implements RelationRenderingStrategy {

    @Override
    public String render(Entity sourceEntity, Relation relation, Entity targetEntity) {
        String targetIdFieldName = NamingUtil.capitalize(getTargetIdFieldName(targetEntity));
        String relationFieldName = NamingUtil.capitalize(relation.getFieldName());
        CollectionType collectionType = relation.getCollectionType();

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

    private String getTargetIdFieldType(Entity targetEntity) {
        return TemplateUtil.getEntityIdType(targetEntity);
    }

}
