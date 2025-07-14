package hr.lknezevic.entitygen.model;

import javax.management.relation.RelationType;

public class Relation {
    private String entity;
    private String referencedEntity;

    private RelationType type;
    private boolean bidirectional;
    private boolean orphanRemoval;
    // private CascadeType cascadeType;

    private String mappedBy;
}
