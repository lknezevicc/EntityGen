package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.AnnotationUtil;

/**
 * Strategy for Embeddable field rendering
 */
public class EmbeddableFieldStrategy implements FieldRenderingStrategy {
    
    @Override
    public String render(Field field) {
        return TemplateFactory.builder()
                .template(TemplateConst.FIELD_COMMENT)
                .build()
                .addParam(field.getColumnName())
                .format() + TemplateConst.NEW_LINE +
                TemplateFactory.builder()
                        .template(TemplateConst.REGULAR_FIELD)
                        .build()
                        .addParams(AnnotationUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                        .format();
    }

}
