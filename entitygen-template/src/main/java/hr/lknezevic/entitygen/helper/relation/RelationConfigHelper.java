package hr.lknezevic.entitygen.helper.relation;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.ForeignKey;

import java.util.List;

/**
 * Helper class for configuring relation properties such as fetch type, cascade type,
 * orphan removal, and collection type based on the relation type and foreign key constraints.
 */
public class RelationConfigHelper {

    /**
     * Returns the default fetch type for relations.
     *
     * @return FetchType.LAZY as the default fetch type.
     */
    public static FetchType getFetchType() {
        return FetchType.LAZY;
    }

    /**
     * Determines the cascade type based on the relation type.
     *
     * @param relationType the type of the relation
     * @return CascadeType based on the relation type
     */
    public static CascadeType getCascadeType(RelationType relationType) {
        return switch (relationType) {
            case ONE_TO_MANY, MANY_TO_MANY, ONE_TO_ONE -> CascadeType.PERSIST_MERGE;
            case MANY_TO_ONE -> CascadeType.PERSIST;
        };
    }

    /**
     * Determines if orphan removal is enabled based on the relation type.
     *
     * @param relationType the type of the relation
     * @return true if orphan removal is enabled, false otherwise
     */
    public static boolean getOrphanRemoval(RelationType relationType) {
        return switch (relationType) {
            case ONE_TO_ONE -> true;
            case ONE_TO_MANY, MANY_TO_ONE, MANY_TO_MANY -> false;
        };
    }

    /**
     * Determines the collection type based on the foreign keys.
     * If all foreign keys are unique, a SET is used; otherwise, a LIST is used.
     *
     * @param foreignKeys the list of foreign keys
     * @return resolved collection type
     */
    public static CollectionType getCollectionType(List<ForeignKey> foreignKeys) {
        if (foreignKeys != null && foreignKeys.stream().allMatch(ForeignKey::isUnique)) {
            return CollectionType.SET;
        }
        
        return CollectionType.LIST;
    }

    /**
     * Checks if a foreign key is optional (nullable).
     *
     * @param foreignKey the foreign key to check
     * @return true if the foreign key is null or nullable, false otherwise
     */
    public static boolean isOptional(ForeignKey foreignKey) {
        return foreignKey == null || foreignKey.isNullable();
    }
    
}
