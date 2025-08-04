package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.AnnotationUtil;

/**
 * Strategy for Entity field rendering - generates field declarations with JPA annotations.
 */
public class EntityFieldStrategy implements FieldRenderingStrategy {
    
    @Override
    public String render(Field field) {
        if (field.isLob()) {
            return TemplateFactory.builder()
                    .template(TemplateConst.LOB_FIELD)
                    .build()
                    .addParams(AnnotationUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                    .format();
        }
        
        return TemplateFactory.builder()
                        .template(TemplateConst.REGULAR_FIELD)
                        .build()
                        .addParams(AnnotationUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                        .format();
    }
}
