package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.modules.strategies.field.DtoFieldStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.field.EmbeddableFieldStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.field.EntityFieldStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.field.ServiceImplFieldStrategy;

/**
 * Factory for creating field rendering strategies based on component type.
 */
public class FieldRenderingFactory {
    
    public static FieldRenderingStrategy createStrategy(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY ->  new EntityFieldStrategy();
            case EMBEDDABLE -> new EmbeddableFieldStrategy();
            case DTO -> new DtoFieldStrategy();
            case SERVICE_IMPL -> new ServiceImplFieldStrategy();
            default -> throw new IllegalArgumentException("Unsupported component type: " + componentType);
        };
    }
}
