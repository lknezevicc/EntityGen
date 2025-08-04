package hr.lknezevic.entitygen.helper.relation;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.ForeignKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class RelationConfigHelper {

    public static FetchType getFetchType() {
        return FetchType.LAZY;
    }

    public static CascadeType getCascadeType(RelationType relationType) {
        return switch (relationType) {
            case MANY_TO_ONE -> CascadeType.PERSIST;
            case ONE_TO_MANY, ONE_TO_ONE -> CascadeType.ALL;
            case MANY_TO_MANY -> CascadeType.PERSIST_MERGE;
        };
    }

    public static boolean getOrphanRemoval(RelationType relationType) {
        return switch (relationType) {
            case ONE_TO_MANY -> true;
            case MANY_TO_ONE, MANY_TO_MANY, ONE_TO_ONE -> false;
        };
    }

    public static CollectionType getCollectionType(List<ForeignKey> foreignKeys) {
        if (foreignKeys != null && foreignKeys.stream().allMatch(ForeignKey::isUnique)) {
            return CollectionType.SET;
        }
        
        return CollectionType.LIST;
    }

    public static CascadeType getCascadeTypeFromConstraints(ForeignKey foreignKey, RelationType relationType) {
        if (foreignKey.isOnDeleteCascade() || foreignKey.isOnUpdateCascade()) {
            return CascadeType.ALL;
        }

        return getCascadeType(relationType);
    }
    
}
