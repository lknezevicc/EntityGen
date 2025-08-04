package hr.lknezevic.entitygen.model.template.modules;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.FieldRenderingFactory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class FieldModule implements ModuleBuilder {
    private final ComponentType componentType;
    private final Field field;
    
    @Override
    public String construct() {
        FieldRenderingStrategy strategy = FieldRenderingFactory.createStrategy(componentType);
        return strategy.render(field);
    }
}
