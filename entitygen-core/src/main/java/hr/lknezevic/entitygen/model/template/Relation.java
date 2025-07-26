package hr.lknezevic.entitygen.model.template;

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

    @Builder.Default
    private Boolean orphanRemoval = false;
    private CascadeType cascadeType;
    private FetchType fetchType;
    @Builder.Default
    private Boolean optional = true;               // za @ManyToOne, @OneToOne

    private CollectionType collectionType;  // za OneToMany / ManyToMany

    // Za JOIN
    @Builder.Default
    private List<String> joinColumns = new ArrayList<>(); // lokalne kolone
    @Builder.Default
    private List<String> referencedColumns = new ArrayList<>();        // strane kolone

    // Za ManyToMany
    private String joinTableName;

    @Builder.Default
    private List<String> inverseJoinColumns = new ArrayList<>();
    @Builder.Default
    private List<String> inverseReferencedColumns = new ArrayList<>();

    private String mapsId; // null ako ne treba MapsId, inače ime polja u embedded id klasi
}
