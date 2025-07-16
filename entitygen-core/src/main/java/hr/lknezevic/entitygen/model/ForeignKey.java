package hr.lknezevic.entitygen.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForeignKey {
    private String name;
    private String fkTable;
    private String fkColumn;
    private String referencedTable;
    private String referencedColumn;

    private boolean onDeleteCascade;
    private boolean onUpdateCascade;
    private boolean notNull;

    private boolean unique;

    private String onDeleteAction;
    private String onUpdateAction;
}
