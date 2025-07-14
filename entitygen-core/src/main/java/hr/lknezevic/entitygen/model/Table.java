package hr.lknezevic.entitygen.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private String schema;
    private String catalog;

    private List<Column> columns = new ArrayList<>();
    private List<ForeignKey> foreignKeys = new ArrayList<>();
    private List<UniqueConstraint> uniqueConstraints = new ArrayList<>();

    public Table(String name, String schema, String catalog) {
        this.name = name;
        this.schema = schema;
        this.catalog = catalog;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }
}
