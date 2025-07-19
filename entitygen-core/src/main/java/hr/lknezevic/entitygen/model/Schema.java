package hr.lknezevic.entitygen.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Schema {
    private String name;
    private String catalog;
    private List<Table> tables = new ArrayList<>();
}
