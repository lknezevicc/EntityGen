package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.domain.Entity;

import java.util.List;

/**
 * Interface for mapping database tables to entity models.
 */
public interface EntityModelMapper {
    List<Entity> mapEntities(List<Table> tables);
}
