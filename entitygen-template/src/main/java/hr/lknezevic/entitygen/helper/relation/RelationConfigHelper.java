package hr.lknezevic.entitygen.helper.relation;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.ForeignKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Helper klasa za pametan odabir relation konfiguracije
 */
@Slf4j
public class RelationConfigHelper {
    
    /**
     * Uvijek LAZY osim ako nije eksplicitno overridden
     */
    public static FetchType getFetchType() {
        return FetchType.LAZY;
    }
    
    /**
     * Pametan odabir cascade type-a ovisno o tipu relacije
     */
    public static CascadeType getCascadeType(RelationType relationType) {
        return switch (relationType) {
            case MANY_TO_ONE -> CascadeType.PERSIST;  // Safe za parent reference
            case ONE_TO_MANY -> CascadeType.ALL;      // Agresivno za child collections
            case ONE_TO_ONE -> CascadeType.ALL;       // Agresivno za 1:1
            case MANY_TO_MANY -> CascadeType.PERSIST; // Safe za many-to-many
        };
    }
    
    /**
     * Pametan odabir orphan removal ovisno o tipu relacije
     */
    public static boolean getOrphanRemoval(RelationType relationType) {
        return switch (relationType) {
            case ONE_TO_MANY -> true;   // Tipično true za parent->children
            case MANY_TO_ONE, MANY_TO_MANY, ONE_TO_ONE -> false; // Sigurno false
        };
    }
    
    /**
     * Pametan odabir collection type-a na temelju table name i foreign key constrainta
     */
    public static CollectionType getCollectionType(String tableName, List<ForeignKey> foreignKeys) {
        // Unique constraints preferiraju SET
        if (foreignKeys != null && foreignKeys.stream().allMatch(ForeignKey::isUnique)) {
            log.debug("Using SET for unique constraint table: {}", tableName);
            return CollectionType.SET;
        }
        
        return CollectionType.LIST; // Default
    }
    
    /**
     * Determiniraj cascade type na temelju database constrainta i relation type
     */
    public static CascadeType getCascadeTypeFromConstraints(ForeignKey foreignKey, RelationType relationType) {
        // Ako DB ima cascade constraints, koristi ALL
        if (foreignKey.isOnDeleteCascade() || foreignKey.isOnUpdateCascade()) {
            return CascadeType.ALL;
        }

        // Inače koristi smart default
        return getCascadeType(relationType);
    }
    
}
