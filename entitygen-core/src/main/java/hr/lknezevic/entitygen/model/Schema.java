package hr.lknezevic.entitygen.model;

import java.util.ArrayList;
import java.util.List;

public class Schema {
    private String name;
    private String catalog;
    private List<Table> tables = new ArrayList<>();

    public Schema(String name, String catalog) {
        this.name = name;
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
}
