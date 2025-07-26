package hr.lknezevic.entitygen.helper;

import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper klasa za detekciju i analizu relacija između entiteta
 */
public class RelationDetector {
    
    /**
     * Detektira je li foreign key jednoznačno mapiran (ONE_TO_ONE ili MANY_TO_ONE)
     */
    public static boolean isOneToOneRelation(Table sourceTable, List<ForeignKey> fkGroup) {
        // 1. Provjeri da li je FK ujedno i PK (shared primary key pattern)
        if (isForeignKeyEqualsPrimaryKey(sourceTable, fkGroup)) {
            return true;
        }
        
        // 2. Provjeri da li su svi FK-ovi unique
        boolean allUnique = fkGroup.stream().allMatch(ForeignKey::isUnique);
        if (allUnique) {
            return true;
        }
        
        // 3. Provjeri da li FK kolone čine unique constraint u source tablici
        Set<String> fkColumns = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());
        
        return sourceTable.getUniqueConstraints().stream()
                .anyMatch(uc -> new HashSet<>(uc.getColumns()).equals(fkColumns));
    }
    
    /**
     * Provjeri da li je foreign key ujedno i primary key
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
     * Detektira je li tablica junction/link tablica za MANY_TO_MANY relaciju
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
     * Pronađi entity po imenu tablice
     */
    public static Optional<Entity> findEntityByTableName(String tableName, List<Entity> entities) {
        return entities.stream()
                .filter(e -> e.getTableName().equalsIgnoreCase(tableName))
                .findFirst();
    }
    
    /**
     * Pronađi tablicu po imenu
     */
    public static Optional<Table> findTableByName(String tableName, List<Table> tables) {
        return tables.stream()
                .filter(t -> t.getName().equalsIgnoreCase(tableName))
                .findFirst();
    }
    
    /**
     * Grupira foreign key-ove po constraint names
     */
    public static Map<String, List<ForeignKey>> groupForeignKeysByConstraint(List<ForeignKey> foreignKeys) {
        return foreignKeys.stream()
                .filter(fk -> fk.getName() != null && !fk.getName().isEmpty())
                .collect(Collectors.groupingBy(ForeignKey::getName));
    }
    
    /**
     * Generiraj jedinstveno ime relacije za označavanje duplikata
     */
    public static String generateRelationKey(String sourceTable, String targetTable, String type) {
        return String.format("%s-%s-%s", sourceTable, targetTable, type);
    }
}
