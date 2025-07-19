package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;

import java.util.List;

public interface EntityModelMapper {
    List<Entity> mapEntities(List<Table> tables);
}
