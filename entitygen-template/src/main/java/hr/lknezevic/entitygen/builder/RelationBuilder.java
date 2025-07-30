package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.helper.RelationContext;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Relation;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class RelationBuilder {
    private final RelationContext relationContext;
    private final List<Relation> relations = new ArrayList<>();
    private final Set<String> processedRelations = new HashSet<>();
    private Table table;

    public RelationBuilder(RelationContext relationContext) {
        this.relationContext = relationContext;
    }

    public RelationBuilder forTable(Table table) {
        this.table = table;
        return this;
    }

    public RelationBuilder forTable(String tableName) {
        this.table = relationContext.getTableByName().get(tableName);
        return this;
    }

    public List<Relation> buildRelations() {
        relations.clear();

        relations.addAll(new ParentToChildRelationBuilder(this).buildSpecificRelations());
        relations.addAll(new InverseRelationBuilder(this).buildSpecificRelations());
        relations.addAll(new ManyToManyRelationBuilder(this).buildSpecificRelations());

        return new ArrayList<>(relations);
    }
}