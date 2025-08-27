package hr.lknezevic.entitygen.analyzer;

import hr.lknezevic.entitygen.analyzer.relation.InverseRelationAnalyzer;
import hr.lknezevic.entitygen.analyzer.relation.ManyToManyRelationAnalyzer;
import hr.lknezevic.entitygen.analyzer.relation.ParentToChildRelationAnalyzer;
import hr.lknezevic.entitygen.analyzer.relation.RelationContext;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.domain.Relation;
import hr.lknezevic.entitygen.utils.LoggingUtility;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Builder for creating relations between entities based on the provided relation context.
 */
@Data
public class RelationBuilder {
    private final RelationContext relationContext;
    private final List<Relation> relations = new ArrayList<>();
    private final Set<String> processedRelations = new HashSet<>();
    private Table table;

    /**
     * Constructs a RelationBuilder for the specified table.
     *
     * @param table the table for which relations are to be built
     */
    public RelationBuilder forTable(Table table) {
        this.table = table;
        return this;
    }

    /**
     * Constructs a RelationBuilder for the specified table name.
     *
     * @param tableName the name of the table for which relations are to be built
     * @return this RelationBuilder instance
     */
    public RelationBuilder forTable(String tableName) {
        this.table = relationContext.getTableByName().get(tableName);
        return this;
    }

    /**
     * Builds and returns a list of relations for the specified table.
     *
     * @return a list of relations for the table
     */
    public List<Relation> buildRelations() {
        relations.clear();

        relations.addAll(new ParentToChildRelationAnalyzer(this).buildSpecificRelations());
        relations.addAll(new InverseRelationAnalyzer(this).buildSpecificRelations());
        relations.addAll(new ManyToManyRelationAnalyzer(this).buildSpecificRelations());

        if (relations.isEmpty())
            LoggingUtility.warn("No relations found for table: " + table.getName());

        return new ArrayList<>(relations);
    }
}