package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.model.template.common.Entity;

/**
 * Strategy interface for rendering primary keys of entities.
 */
public interface PrimaryKeyRenderingStrategy {
    String render(Entity entity);
}
