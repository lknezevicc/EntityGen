package hr.lknezevic.entitygen.rendering.strategies.field;

import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.model.domain.Field;
import hr.lknezevic.entitygen.rendering.strategies.FieldRenderingStrategy;

/**
 * Strategy for DTO field rendering.
 */
public class DtoFieldStrategy implements FieldRenderingStrategy {

    /**
     * Renders a field for a DTO (Data Transfer Object) using a predefined template.
     * @param field the field to be rendered
     * @return a formatted string representing the field in the DTO
     */
    @Override
    public String render(Field field) {
        return TemplateFactory.builder()
                .template(TemplateConst.SIMPLE_FIELD)
                .build()
                .addParams(field.getJavaType(), field.getName())
                .format();
    }
}
