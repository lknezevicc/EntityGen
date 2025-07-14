package hr.lknezevic.entitygen.model;

import java.util.List;

public class Column {
    private String name;
    private String type;
    private boolean nullable;
    private boolean primaryKey;
    private boolean autoIncrement;
    private boolean unique;
    private String defaultValue;
    private String checkConstraint;
    private String comment;

    private Integer length;
    private Integer precision;
    private Integer scale;
    private List<String> enumValues;

    private boolean unsigned;
    private boolean generated;
}
