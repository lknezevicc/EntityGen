package hr.lknezevic.entitygen.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
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

    public List<String> getPrimaryKeys() {
        return columns.stream().filter(Column::isPrimaryKey).map(Column::getName).toList();
    }
}
