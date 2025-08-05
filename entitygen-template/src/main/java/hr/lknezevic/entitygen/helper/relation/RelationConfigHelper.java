package hr.lknezevic.entitygen.helper.relation;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.ForeignKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Helper class for configuring relation properties such as fetch type, cascade type,
 * orphan removal, and collection type based on the relation type and foreign key constraints.
 *
 * @author leonknezevic
 */
@Slf4j
public class RelationConfigHelper {

    public static FetchType getFetchType() {
        return FetchType.LAZY;
    }

    public static CascadeType getCascadeType(RelationType relationType, ForeignKey foreignKey) {
        if (foreignKey != null && (foreignKey.isOnDeleteCascade() || foreignKey.isOnUpdateCascade())) {
            return switch (relationType) {
                case ONE_TO_MANY, ONE_TO_ONE -> CascadeType.ALL;
                case MANY_TO_ONE -> CascadeType.PERSIST;
                case MANY_TO_MANY -> CascadeType.PERSIST_MERGE;
            };
        }

        return switch (relationType) {
            case ONE_TO_MANY, MANY_TO_MANY, ONE_TO_ONE -> CascadeType.PERSIST_MERGE;
            case MANY_TO_ONE -> CascadeType.PERSIST;
        };
    }

    public static boolean getOrphanRemoval(RelationType relationType, ForeignKey foreignKey) {
        boolean hasDeleteCascade = foreignKey != null && foreignKey.isOnDeleteCascade();

        return switch (relationType) {
            case ONE_TO_MANY, ONE_TO_ONE -> hasDeleteCascade;
            case MANY_TO_ONE, MANY_TO_MANY -> false;
        };
    }

    public static CollectionType getCollectionType(List<ForeignKey> foreignKeys) {
        if (foreignKeys != null && foreignKeys.stream().allMatch(ForeignKey::isUnique)) {
            return CollectionType.SET;
        }
        
        return CollectionType.LIST;
    }

    public static boolean isOptional(ForeignKey foreignKey) {
        return foreignKey == null || foreignKey.isNullable();
    }
    
}
