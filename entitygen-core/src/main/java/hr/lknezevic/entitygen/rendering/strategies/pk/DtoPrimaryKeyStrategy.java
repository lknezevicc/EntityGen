package hr.lknezevic.entitygen.rendering.strategies.pk;

import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.rendering.strategies.PrimaryKeyRenderingStrategy;
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
