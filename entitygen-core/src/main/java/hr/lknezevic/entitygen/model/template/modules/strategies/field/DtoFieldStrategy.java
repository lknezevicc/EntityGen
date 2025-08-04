package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;

/**
 * Strategy for DTO field rendering
 */
public class DtoFieldStrategy implements FieldRenderingStrategy {
    
    @Override
    public String render(Field field) {
        return TemplateFactory.builder()
                .template(TemplateConst.SIMPLE_FIELD)
                .build()
                .addParams(field.getJavaType(), field.getName())
                .format();
    }
}
