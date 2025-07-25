package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.List;

public interface RelationResolver {
    /**
     * Izgradi sve relations za jedan entitet (na temelju table definicije i popisa svih tablica/entiteta).
     */
    List<Relation> buildRelations(Table table, List<Table> allTables, List<Entity> allEntities);
}
