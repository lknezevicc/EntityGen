package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.DtoPrimaryKeyStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.EntityPrimaryKeyStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.ServiceImplPrimaryKeyStrategy;

/**
 * Factory class for creating primary key rendering strategies based on the component type.
 */
public class PrimaryKeyRenderingFactory {

    /**
     * Creates a primary key rendering strategy based on the provided component type.
     *
     * @param componentType the type of component
     * @return an instance of PrimaryKeyRenderingStrategy
     * @throws IllegalArgumentException if the component type is not supported
     */
    public static PrimaryKeyRenderingStrategy createStrategy(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY -> new EntityPrimaryKeyStrategy();
            case DTO -> new DtoPrimaryKeyStrategy();
            case SERVICE_IMPL -> new ServiceImplPrimaryKeyStrategy();
            default -> throw new IllegalArgumentException("Unsupported component type for primary key rendering: " + componentType);
        };
    }
}
