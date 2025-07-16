package hr.lknezevic.entitygen.model;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.RelationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Relation {
    private String entity;
    private String referencedEntity;

    private RelationType type;
    private boolean bidirectional;
    private boolean orphanRemoval;
    private CascadeType cascadeType;

    private String mappedBy;
}
