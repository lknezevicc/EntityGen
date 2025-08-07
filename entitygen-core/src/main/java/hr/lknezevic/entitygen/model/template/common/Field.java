package hr.lknezevic.entitygen.model.template.common;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a field in the context of a database entity.
 * Contains metadata about the field such as its name, column name,
 * Java type, length, precision, scale, and various constraints.
 */
@Data
@Builder
public class Field {
    private String name;
    private String columnName;

    private String javaType;
    private Integer length;
    private Integer precision;
    private Integer scale;

    private boolean primaryKey;
    private boolean nullable;
    private boolean unique;
    private boolean autoIncrement;
    private boolean lob;

    private String defaultValue;
    private String comment;
}
