package hr.lknezevic.entitygen.model;

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

    public ForeignKey(String name, String fkTable, String fkColumn, String referencedTable, String referencedColumn) {
        this.name = name;
        this.fkTable = fkTable;
        this.fkColumn = fkColumn;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFkTable() {
        return fkTable;
    }

    public void setFkTable(String fkTable) {
        this.fkTable = fkTable;
    }

    public String getFkColumn() {
        return fkColumn;
    }

    public void setFkColumn(String fkColumn) {
        this.fkColumn = fkColumn;
    }

    public String getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(String referencedTable) {
        this.referencedTable = referencedTable;
    }

    public String getReferencedColumn() {
        return referencedColumn;
    }

    public void setReferencedColumn(String referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public boolean isOnDeleteCascade() {
        return onDeleteCascade;
    }

    public void setOnDeleteCascade(boolean onDeleteCascade) {
        this.onDeleteCascade = onDeleteCascade;
    }

    public boolean isOnUpdateCascade() {
        return onUpdateCascade;
    }

    public void setOnUpdateCascade(boolean onUpdateCascade) {
        this.onUpdateCascade = onUpdateCascade;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }
}
