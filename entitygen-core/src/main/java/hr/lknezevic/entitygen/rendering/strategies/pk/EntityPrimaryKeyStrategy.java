package hr.lknezevic.entitygen.rendering.strategies.pk;

import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.model.domain.Field;
import hr.lknezevic.entitygen.rendering.strategies.PrimaryKeyRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering primary keys of entities.
 */
public class EntityPrimaryKeyStrategy implements PrimaryKeyRenderingStrategy {

    /**
     * Renders a primary key for an entity using a predefined template.
     *
     * @param entity the entity whose primary key is to be rendered
     * @return a formatted string representing the primary key in the entity
     */
    @Override
    public String render(Entity entity) {
        String idType = TemplateUtil.getEntityIdType(entity);

        if (entity.isCompositeKey()) {
            return TemplateFactory.builder()
                    .template(TemplateConst.COMPOSITE_PRIMARY_KEY)
                    .build()
                    .addParams(idType, NamingUtil.uncapitalize(entity.getEmbeddedId().getClassName()), idType)
                    .format();
        }

        Field pkField = entity.getPrimaryKeyFields().getFirst();
        boolean isAutoincrement = pkField.isAutoIncrement();

        if (isAutoincrement) {
            return TemplateFactory.builder()
                    .template(TemplateConst.AUTO_INCREMENT_PRIMARY_KEY)
                    .build()
                    .addParam(pkField.getColumnName())
                    .addParam(idType)
                    .addParam(pkField.getName())
                    .format();
        }

        return TemplateFactory.builder()
                .template(TemplateConst.SIMPLE_PRIMARY_KEY)
                .build()
                .addParams(pkField.getColumnName())
                .addParams(idType)
                .addParam(pkField.getName())
                .format();
    }
}
