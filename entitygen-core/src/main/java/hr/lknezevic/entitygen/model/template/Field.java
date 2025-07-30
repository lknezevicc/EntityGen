package hr.lknezevic.entitygen.model.template;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Field {
    private String name;
    private String columnName;

    private String javaType;
    private String sqlType;
    private Integer length;
    private Integer precision;
    private Integer scale;

    private boolean primaryKey;
    private boolean nullable;
    private boolean unique;
    private boolean autoIncrement;
    private boolean generated;
    private boolean isLob;          // true for TEXT, CLOB, LONGTEXT, etc.

    private String defaultValue;
    private String comment;
}
