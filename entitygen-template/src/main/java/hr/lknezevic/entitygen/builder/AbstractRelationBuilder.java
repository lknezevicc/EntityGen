package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.RelationConfigHelper;
import hr.lknezevic.entitygen.helper.RelationContext;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.List;
import java.util.Set;

public abstract class AbstractRelationBuilder {
    private final RelationBuilder parent;
    protected final Table table;

    public AbstractRelationBuilder(RelationBuilder parent) {
        this.parent = parent;
        this.table = parent.getTable();
    }

    protected abstract List<Relation> buildSpecificRelations();

    protected RelationContext getContext() {
        return parent.getRelationContext();
    }

    protected Set<String> getProcessedRelations() {
        return parent.getProcessedRelations();
    }

    protected void addProcessedRelation(String relationKey) {
        parent.getProcessedRelations().add(relationKey);
    }

    protected boolean isRelationProcessed(String relationKey) {
        return parent.getProcessedRelations().contains(relationKey);
    }

    // Helper methods za dobivanje smart config vrijednosti
    protected FetchType getFetchType() {
        return RelationConfigHelper.getFetchType();
    }
    
    protected CascadeType getCascadeType(RelationType relationType) {
        return RelationConfigHelper.getCascadeType(relationType);
    }
    
    protected boolean getOrphanRemoval(RelationType relationType) {
        return RelationConfigHelper.getOrphanRemoval(relationType);
    }
    
    protected CollectionType getCollectionType(String tableName, List<ForeignKey> foreignKeys) {
        return RelationConfigHelper.getCollectionType(tableName, foreignKeys);
    }
}
