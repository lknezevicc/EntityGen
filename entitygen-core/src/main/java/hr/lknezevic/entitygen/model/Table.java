package hr.lknezevic.entitygen.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a database table with its name, schema, catalog, columns, foreign keys, and unique constraints.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Table {
    private String name;
    private String schema;
    private String catalog;

    @Builder.Default
    private List<Column> columns = new ArrayList<>();
    @Builder.Default
    private List<ForeignKey> foreignKeys = new ArrayList<>();
    @Builder.Default
    private List<UniqueConstraint> uniqueConstraints = new ArrayList<>();

    /**
     * Returns a list of column names that are primary keys in this table.
     *
     * @return a list of primary key column names
     */
    public List<String> getPrimaryKeys() {
        return columns.stream().filter(Column::isPrimaryKey).map(Column::getName).toList();
    }
}
