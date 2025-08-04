package hr.lknezevic.entitygen.model.template.modules;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.PrimaryKeyRenderingFactory;
import lombok.Builder;

@Builder
public class PrimaryKeyModule implements ModuleBuilder {
    private final ComponentType componentType;
    private final Entity entity;

    @Override
    public String construct() {
        PrimaryKeyRenderingStrategy strategy = PrimaryKeyRenderingFactory.createStrategy(componentType);
        return strategy.render(entity);
    }

}
