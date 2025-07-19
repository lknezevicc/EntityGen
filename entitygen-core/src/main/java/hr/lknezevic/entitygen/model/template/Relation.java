package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relation {
    private String fieldName;
    private String targetEntityClass;

    private RelationType type;              // ONE_TO_ONE, MANY_TO_ONE, etc.
    private boolean bidirectional;
    private String mappedBy;

    private boolean orphanRemoval;
    private CascadeType cascadeType;
    private Boolean optional;               // za @ManyToOne, @OneToOne

    private CollectionType collectionType;  // za OneToMany / ManyToMany

    private String joinColumnName;          // FK u ovoj tablici
    private String referencedColumnName;    // PK u target tablici

    private String joinTableName;           // za ManyToMany
    private String inverseJoinColumnName;   // za ManyToMany
    private String inverseReferencedColumnName;

    private List<JoinColumn> joinColumns; // podrška za composite FK
}
