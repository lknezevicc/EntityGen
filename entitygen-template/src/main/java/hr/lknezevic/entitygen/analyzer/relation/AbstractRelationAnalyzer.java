package hr.lknezevic.entitygen.analyzer.relation;

import hr.lknezevic.entitygen.analyzer.RelationBuilder;
import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.relation.RelationConfigHelper;
import hr.lknezevic.entitygen.model.RelationContext;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;

import java.util.List;
import java.util.Set;

/**
 * Abstract class for building relations in an entity.
 * Provides common functionality for relation builders.
 */
public abstract class AbstractRelationAnalyzer implements RelationAnalyzer {
    private final RelationBuilder parent;
    protected final Table table;

    public AbstractRelationAnalyzer(RelationBuilder parent) {
        this.parent = parent;
        this.table = parent.getTable();
    }

    protected RelationContext getContext() {
        return parent.getRelationContext();
    }

    protected Set<String> getProcessedRelations() {
        return parent.getProcessedRelations();
    }

    protected void addProcessedRelation(String relationKey) {
        parent.getProcessedRelations().add(relationKey);
    }

    protected boolean relationNotProcessed(String relationKey) {
        return !parent.getProcessedRelations().contains(relationKey);
    }

    protected FetchType getFetchType() {
        return RelationConfigHelper.getFetchType();
    }
    
    protected CascadeType getCascadeType(RelationType relationType) {
        return RelationConfigHelper.getCascadeType(relationType);
    }
    
    protected boolean getOrphanRemoval(RelationType relationType) {
        return RelationConfigHelper.getOrphanRemoval(relationType);
    }
    
    protected CollectionType getCollectionType(List<ForeignKey> foreignKeys) {
        return RelationConfigHelper.getCollectionType(foreignKeys);
    }

    protected boolean isOptional(ForeignKey foreignKey) {
        return RelationConfigHelper.isOptional(foreignKey);
    }
}
