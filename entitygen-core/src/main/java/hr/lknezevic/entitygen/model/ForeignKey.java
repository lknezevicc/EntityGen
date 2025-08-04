package hr.lknezevic.entitygen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a foreign key constraint in a database table.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForeignKey {
    private String constraintName;
    private String fkColumn;
    private String referencedTable;
    private String referencedColumn;
    private boolean nullable;
    private boolean unique;
    private boolean onDeleteCascade;
    private boolean onUpdateCascade;
}
