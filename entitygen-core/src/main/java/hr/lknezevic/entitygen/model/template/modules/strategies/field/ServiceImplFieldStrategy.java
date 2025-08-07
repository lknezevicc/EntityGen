package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;

/**
 * Strategy for rendering service implementation fields.
 */
public class ServiceImplFieldStrategy implements FieldRenderingStrategy {

    /**
     * Renders a field for a service implementation using a predefined template.
     *
     * @param field the field to be rendered
     * @return a formatted string representing the field in the service implementation
     */
    @Override
    public String render(Field field) {
        return TemplateFactory.builder()
                .template(TemplateConst.SERVICE_GETTER_FIELD)
                .build()
                .addParam(NamingUtil.capitalize(field.getName()))
                .format();
    }
}
