package hr.lknezevic.entitygen.rendering.strategies.field;

import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.model.domain.Field;
import hr.lknezevic.entitygen.rendering.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering embeddable fields in entities.
 */
public class EmbeddableFieldStrategy implements FieldRenderingStrategy {

    /**
     * Renders a field for an embeddable entity using a predefined template.
     *
     * @param field the field to be rendered
     * @return a formatted string representing the field in the embeddable entity
     */
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
                        .addParams(TemplateUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                        .format();
    }

}
