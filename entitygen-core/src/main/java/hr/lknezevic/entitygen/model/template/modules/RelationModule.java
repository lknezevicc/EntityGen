package hr.lknezevic.entitygen.model.template.modules;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.model.template.modules.strategies.RelationRenderingFactory;
import hr.lknezevic.entitygen.model.template.modules.strategies.RelationRenderingStrategy;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Represents a module that constructs a relation between entities.
 * It uses a rendering strategy based on the component type to generate the relation representation.
 */
@Builder
@RequiredArgsConstructor
public class RelationModule implements ModuleBuilder {
    private final ComponentType componentType;
    private final Relation relation;
    private final Entity sourceEntity;
    private final Entity targetEntity;

    /**
     * Constructs a relation representation based on the component type and relation details.
     *
     * @return a string representation of the relation
     */
    @Override
    public String construct() {
        RelationRenderingStrategy strategy = RelationRenderingFactory.createStrategy(componentType);
        return strategy.render(sourceEntity, relation, targetEntity);
    }

}
