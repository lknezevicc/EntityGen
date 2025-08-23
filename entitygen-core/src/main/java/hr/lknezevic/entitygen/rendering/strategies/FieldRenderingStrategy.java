package hr.lknezevic.entitygen.rendering.strategies;

import hr.lknezevic.entitygen.model.domain.Field;

/**
 * Strategy interface for different field rendering approaches.
 */
public interface FieldRenderingStrategy {
    String render(Field field);
}
