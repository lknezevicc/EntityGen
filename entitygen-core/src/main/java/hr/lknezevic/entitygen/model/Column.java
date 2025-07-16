package hr.lknezevic.entitygen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a column in a database table.
 * This class encapsulates various properties of a column such as its name, type,
 * constraints, and other attributes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
