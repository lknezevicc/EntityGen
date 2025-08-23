package hr.lknezevic.entitygen.model;

import hr.lknezevic.entitygen.model.domain.Entity;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Context for managing relations between entities and tables.
 * It holds all tables, all entities, and provides mappings for quick access
 * to entities by table name and tables by name.
 */
@Data
public class RelationContext {
    private final List<Table> allTables;
    private final List<Entity> allEntities;
    private final Map<String, Entity> entityByTableName;
    private final Map<String, Table> tableByName;
    
    public RelationContext(List<Table> allTables, List<Entity> allEntities) {
        this.allTables = allTables;
        this.allEntities = allEntities;
        this.entityByTableName = allEntities.stream()
                .collect(Collectors.toMap(
                    Entity::getTableName, 
                    e -> e, 
                    (existing, replacement) -> existing
                ));
        this.tableByName = allTables.stream()
                .collect(Collectors.toMap(
                    Table::getName, 
                    t -> t, 
                    (existing, replacement) -> existing
                ));
    }
}
