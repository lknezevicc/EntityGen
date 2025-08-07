package hr.lknezevic.entitygen.model.template.modules;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingFactory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Represents a module that constructs the primary key representation for an entity.
 * It uses a rendering strategy based on the component type to generate the primary key representation.
 */
@Builder
@RequiredArgsConstructor
public class PrimaryKeyModule implements ModuleBuilder {
    private final ComponentType componentType;
    private final Entity entity;

    /**
     * Constructs the primary key representation for the entity based on the component type.
     *
     * @return a string representation of the primary key
     */
    @Override
    public String construct() {
        PrimaryKeyRenderingStrategy strategy = PrimaryKeyRenderingFactory.createStrategy(componentType);
        return strategy.render(entity);
    }

}
