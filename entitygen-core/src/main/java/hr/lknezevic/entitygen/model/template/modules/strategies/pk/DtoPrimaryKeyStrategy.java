package hr.lknezevic.entitygen.model.template.modules.strategies.pk;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingStrategy;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering primary keys in DTOs.
 */
public class DtoPrimaryKeyStrategy implements PrimaryKeyRenderingStrategy {

    /**
     * Renders a primary key for a DTO (Data Transfer Object) using a predefined template.
     *
     * @param entity the entity whose primary key is to be rendered
     * @return a formatted string representing the primary key in the DTO
     */
    @Override
    public String render(Entity entity) {
        String idType = TemplateUtil.getEntityIdType(entity);
        String name;

        if (entity.isCompositeKey()) {
            name = entity.getEmbeddedId().getClassName();
        } else {
            name = entity.getPrimaryKeyFields().getFirst().getName();
        }

        return TemplateFactory.builder()
                .template(TemplateConst.SIMPLE_FIELD)
                .build()
                .addParams(idType, name)
                .format();
    }

}
