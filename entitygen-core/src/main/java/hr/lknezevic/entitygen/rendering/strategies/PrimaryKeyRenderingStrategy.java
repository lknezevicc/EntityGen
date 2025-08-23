package hr.lknezevic.entitygen.rendering.strategies;

import hr.lknezevic.entitygen.model.domain.Entity;

/**
 * Strategy interface for rendering primary keys of entities.
 */
public interface PrimaryKeyRenderingStrategy {
    String render(Entity entity);
}
