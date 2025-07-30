package hr.lknezevic.entitygen.enums;

public enum RelationType {
    ONE_TO_ONE,
    ONE_TO_MANY,
    MANY_TO_ONE,
    MANY_TO_MANY,
    MANY_TO_MANY_DIRECT  // For direct @ManyToMany without association entity
}
