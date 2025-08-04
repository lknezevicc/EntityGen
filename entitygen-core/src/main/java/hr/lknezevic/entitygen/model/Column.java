package hr.lknezevic.entitygen.model;

import lombok.*;

/**
 * Represents a column in a database table.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Column {
    private String name;
    private Integer dataType;
    private String javaType;

    private boolean nullable;
    private boolean primaryKey;
    private boolean autoIncrement;
    private boolean unique;

    private Integer length;
    private Integer precision;
    private Integer scale;

    private String defaultValue;
    private String comment;
    private boolean isLob;
}
