package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.model.template.common.Field;

/**
 * Strategy interface for different field rendering approaches.
 */
public interface FieldRenderingStrategy {
    String render(Field field);
}
