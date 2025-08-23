package hr.lknezevic.entitygen.rendering.strategies.pk;

import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.rendering.strategies.PrimaryKeyRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;

/**
 * Strategy for rendering primary key fields in service implementation.
 */
public class ServiceImplPrimaryKeyStrategy implements PrimaryKeyRenderingStrategy {

    /**
     * Renders a primary key field for a service implementation using a predefined template.
     *
     * @param entity the entity whose primary key is to be rendered
     * @return a formatted string representing the primary key field in the service implementation
     */
    @Override
    public String render(Entity entity) {
        String name = entity.isCompositeKey() ?
                NamingUtil.capitalize(entity.getEmbeddedId().getClassName()) :
                NamingUtil.capitalize(entity.getPrimaryKeyFields().getFirst().getName());

        return TemplateFactory.builder()
                .template(TemplateConst.SERVICE_GETTER_FIELD)
                .build()
                .addParams(name)
                .format();
    }

}
