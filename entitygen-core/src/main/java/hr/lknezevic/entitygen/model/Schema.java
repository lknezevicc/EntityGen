package hr.lknezevic.entitygen.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@ToString
public class Schema {
    private String name;
    private String catalog;
    @Builder.Default
    private List<Table> tables = new ArrayList<>();
}
