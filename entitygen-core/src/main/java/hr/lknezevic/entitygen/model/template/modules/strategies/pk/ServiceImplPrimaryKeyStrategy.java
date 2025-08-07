package hr.lknezevic.entitygen.model.template.modules.strategies.pk;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingStrategy;
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
