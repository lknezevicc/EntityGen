package hr.lknezevic.entitygen.rendering.strategies;

import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.model.domain.Relation;

/**
 * Strategy interface for rendering relations between entities.
 */
public interface RelationRenderingStrategy {
    String render(Entity sourceEntity, Relation relation, Entity targetEntity);
}
