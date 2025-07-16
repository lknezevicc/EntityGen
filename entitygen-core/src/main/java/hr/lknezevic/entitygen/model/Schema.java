package hr.lknezevic.entitygen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Schema {
    private String name;
    private String catalog;
    private List<Table> tables = new ArrayList<>();
}
