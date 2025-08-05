package hr.lknezevic.entitygen.model.template.common;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a relation between entities in the context of a database.
 * Contains metadata about the relation such as field name, target entity class,
 * type of relation, cascade type, fetch type, and join columns.
 *
 * @author leonknezevic
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relation {
    private String fieldName;
    private String targetEntityClass;

    private RelationType type;
    private String mappedBy;
    private boolean selfReferencing;

    @Builder.Default
    private Boolean orphanRemoval = false;
    private CascadeType cascadeType;
    private FetchType fetchType;
    @Builder.Default
    private Boolean optional = true;

    private CollectionType collectionType;

    @Builder.Default
    private List<String> joinColumns = new ArrayList<>();
    @Builder.Default
    private List<String> referencedColumns = new ArrayList<>();

    private String joinTableName;

    @Builder.Default
    private List<String> inverseJoinColumns = new ArrayList<>();
    @Builder.Default
    private List<String> inverseReferencedColumns = new ArrayList<>();

    private String mapsId;
}
