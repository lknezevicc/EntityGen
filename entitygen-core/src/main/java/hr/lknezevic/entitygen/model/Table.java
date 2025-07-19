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

    private List<Column> columns = new ArrayList<>();
    private List<ForeignKey> foreignKeys = new ArrayList<>();
    private List<UniqueConstraint> uniqueConstraints = new ArrayList<>();
}
