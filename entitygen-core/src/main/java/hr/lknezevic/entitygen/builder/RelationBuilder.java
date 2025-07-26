package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.helper.RelationContext;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class RelationBuilder {
    private final RelationConfig config;
    private final RelationContext context;

    /**
     * Kreće proces buildovanja relacija za određenu tabelu
     */
    public TableRelationBuilder forTable(Table table) {
        return new TableRelationBuilder(this, table);
    }

    /**
     * Kreće proces buildovanja relacija za određenu tabelu (po imenu)
     */
    public TableRelationBuilder forTable(String tableName) {
        Table table = context.getTableByName().get(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table not found: " + tableName);
        }
        return forTable(table);
    }

    /**
     * Nested builder class za rad sa određenom tabelom
     */
    @RequiredArgsConstructor
    public static class TableRelationBuilder {
        private final RelationBuilder parent;
        private final Table table;

        /**
         * Builds ManyToMany relations for current table
         */
        public ManyToManyBuilder buildManyToMany() {
            return new ManyToManyBuilder(parent, table);
        }

        /**
         * Builds OneToMany relations for current table
         */
        public OneToManyBuilder buildOneToMany() {
            return new OneToManyBuilder(parent, table);
        }

        /**
         * Builds ManyToOne relations for current table
         */
        public ManyToOneBuilder buildManyToOne() {
            return new ManyToOneBuilder(parent, table);
        }

        /**
         * Builds OneToOne relations for current table
         */
        public OneToOneBuilder buildOneToOne() {
            return new OneToOneBuilder(parent, table);
        }

        /**
         * Finalizes and returns all built relations for this table
         */
        public List<Relation> build() {
            Entity entity = parent.context.getEntityByTableName().get(table.getName());
            if (entity == null) {
                throw new IllegalStateException("Entity not found for table: " + table.getName());
            }
            return entity.getRelations();
        }
    }

    // Getter methods za pristup config i context iz builder klasa
    public RelationConfig getConfig() {
        return config;
    }

    public RelationContext getContext() {
        return context;
    }
}
