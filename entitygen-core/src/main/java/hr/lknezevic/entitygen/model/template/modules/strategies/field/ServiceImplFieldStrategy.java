package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.NamingUtil;

/**
 * Strategy for ServiceImpl field rendering - generates entity.getFieldName() calls.
 */
public class ServiceImplFieldStrategy implements FieldRenderingStrategy {
    
    @Override
    public String render(Field field) {
        return TemplateFactory.builder()
                .template(TemplateConst.SERVICE_GETTER_FIELD)
                .build()
                .addParam(NamingUtil.capitalize(field.getName()))
                .format();
    }
}
