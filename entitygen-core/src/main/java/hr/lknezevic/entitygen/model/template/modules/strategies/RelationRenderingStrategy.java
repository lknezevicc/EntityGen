package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;

/**
 * Strategy interface for rendering relations between entities.
 *
 * @author leonknezevic
 */
public interface RelationRenderingStrategy {
    String render(Entity sourceEntity, Relation relation, Entity targetEntity);
}
