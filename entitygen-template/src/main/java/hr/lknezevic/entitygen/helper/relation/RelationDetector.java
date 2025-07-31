package hr.lknezevic.entitygen.helper.relation;

import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for detecting and analyzing relationships between database entities.
 * <p>
 * Provides static methods for identifying relationship types (ONE_TO_ONE, MANY_TO_ONE, MANY_TO_MANY)
 * based on foreign key constraints, unique constraints, and table structure analysis.
 *
 * @author Leon Knežević
 */
public class RelationDetector {
    
    /**
     * Determines if a foreign key represents a ONE_TO_ONE relationship.
     *
     * @param sourceTable the table containing the foreign key
     * @param fkGroup list of foreign key columns that form the relationship
     * @return true if this represents a ONE_TO_ONE relationship
     */
    public static boolean isOneToOneRelation(Table sourceTable, List<ForeignKey> fkGroup) {
        // je li FK ujedno i PK (shared primary key pattern)
        if (isForeignKeyEqualsPrimaryKey(sourceTable, fkGroup)) {
            return true;
        }
        
        // svi FK-ovi unique?
        boolean allUnique = fkGroup.stream().allMatch(ForeignKey::isUnique);
        if (allUnique) {
            return true;
        }
        
        // jesu li FK kolone unique constraint u source tablici
        Set<String> fkColumns = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());
        
        return sourceTable.getUniqueConstraints().stream()
                .anyMatch(uc -> new HashSet<>(uc.getColumns()).equals(fkColumns));
    }
    
    /**
     * Checks if foreign key columns are also primary key columns (shared primary key pattern).
     *
     * @param table the table to check
     * @param fkGroup foreign key columns to compare with primary key
     * @return true if foreign key equals primary key
     */
    public static boolean isForeignKeyEqualsPrimaryKey(Table table, List<ForeignKey> fkGroup) {
        Set<String> fkCols = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        
        Set<String> pkCols = new HashSet<>(table.getPrimaryKeys());
        
        return !fkCols.isEmpty() && fkCols.equals(pkCols);
    }
    
    /**
     * Determines if a table serves as a junction table for MANY_TO_MANY relationships.
     * <p>
     * Junction tables typically have exactly two foreign keys referencing different tables,
     * with those foreign key columns forming part of the primary key.
     *
     * @param table the table to analyze
     * @return true if this is a junction table
     */
    public static boolean isJunctionTable(Table table) {
        // Junction tablica mora imati točno 2 foreign key-a
        if (table.getForeignKeys().size() != 2) {
            return false;
        }
        
        // FK-ovi moraju referencirati različite tablice
        List<ForeignKey> foreignKeys = table.getForeignKeys();
        String refTable1 = foreignKeys.get(0).getReferencedTable();
        String refTable2 = foreignKeys.get(1).getReferencedTable();
        
        if (refTable1.equals(refTable2)) {
            return false; // Self-referencing nije junction tablica
        }
        
        // PK mora se sastojati od FK kolona (može imati additional data)
        Set<String> fkColumns = foreignKeys.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());
        
        Set<String> pkColumns = new HashSet<>(table.getPrimaryKeys());
        
        // FK kolone moraju biti dio PK-a
        return pkColumns.containsAll(fkColumns);
    }
    
    /**
     * Finds an entity by its table name.
     *
     * @param tableName name of the table to find
     * @param entities list of entities to search
     * @return Optional containing the entity if found
     */
    public static Optional<Entity> findEntityByTableName(String tableName, List<Entity> entities) {
        return entities.stream()
                .filter(e -> e.getTableName().equalsIgnoreCase(tableName))
                .findFirst();
    }
    
    /**
     * Groups foreign keys by their constraint names.
     *
     * @param foreignKeys list of foreign keys to group
     * @return map of constraint names and their foreign keys
     */
    public static Map<String, List<ForeignKey>> groupForeignKeysByConstraint(List<ForeignKey> foreignKeys) {
        return foreignKeys.stream()
                .filter(fk -> fk.getName() != null && !fk.getName().isEmpty())
                .collect(Collectors.groupingBy(ForeignKey::getName));
    }
    
    /**
     * Generates a unique relationship key for duplicate detection.
     *
     * @param sourceTable name of the source table
     * @param targetTable name of the target table  
     * @param type relationship type
     * @return unique string key
     */
    public static String generateRelationKey(String sourceTable, String targetTable, String type) {
        return String.format("%s-%s-%s", sourceTable, targetTable, type);
    }
}
