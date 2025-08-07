package hr.lknezevic.entitygen.model.template.modules.strategies.field;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.utils.TemplateUtil;

/**
 * Strategy for rendering entity fields.
 */
public class EntityFieldStrategy implements FieldRenderingStrategy {

    /**
     * Renders a field for an entity using a predefined template.
     *
     * @param field the field to be rendered
     * @return a formatted string representing the field in the entity
     */
    @Override
    public String render(Field field) {
        if (field.isLob()) {
            return TemplateFactory.builder()
                    .template(TemplateConst.LOB_FIELD)
                    .build()
                    .addParams(TemplateUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                    .format();
        }
        
        return TemplateFactory.builder()
                        .template(TemplateConst.REGULAR_FIELD)
                        .build()
                        .addParams(TemplateUtil.buildColumnParams(field), field.getJavaType(), field.getName())
                        .format();
    }
}
