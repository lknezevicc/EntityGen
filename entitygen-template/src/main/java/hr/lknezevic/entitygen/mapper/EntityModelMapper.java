package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Field;

import java.util.List;

public interface EntityModelMapper {
    List<Entity> mapEntities(List<Table> tables);

    default boolean checkCompositeKey(List<Field> fields) {
        return fields.stream().filter(Field::isPrimaryKey).count() > 1;
    }

}
