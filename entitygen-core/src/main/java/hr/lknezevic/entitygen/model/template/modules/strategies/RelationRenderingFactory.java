package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.modules.strategies.relation.DtoRelationRendering;
import hr.lknezevic.entitygen.model.template.modules.strategies.relation.EntityRelationRendering;
import hr.lknezevic.entitygen.model.template.modules.strategies.relation.ServiceImplRelationRendering;

/**
 * Factory class for creating relation rendering strategies based on the component type.
 */
public class RelationRenderingFactory {

    /**
     * Creates a relation rendering strategy based on the provided component type.
     *
     * @param componentType the type of component
     * @return a RelationRenderingStrategy instance
     * @throws IllegalArgumentException if the component type is unsupported
     */
    public static RelationRenderingStrategy createStrategy(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY -> new EntityRelationRendering();
            case DTO -> new DtoRelationRendering();
            case SERVICE_IMPL -> new ServiceImplRelationRendering();
            default -> throw new IllegalArgumentException("Unsupported component type: " + componentType);
        };
    }
}
