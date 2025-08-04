package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.DtoPrimaryKeyStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.EntityPrimaryKeyStrategy;
import hr.lknezevic.entitygen.model.template.modules.strategies.pk.ServiceImplPrimaryKeyStrategy;

public class PrimaryKeyRenderingFactory {

    public static PrimaryKeyRenderingStrategy createStrategy(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY -> new EntityPrimaryKeyStrategy();
            case DTO -> new DtoPrimaryKeyStrategy();
            case SERVICE_IMPL -> new ServiceImplPrimaryKeyStrategy();
            default -> throw new IllegalArgumentException("Unsupported component type for primary key rendering: " + componentType);
        };
    }
}
