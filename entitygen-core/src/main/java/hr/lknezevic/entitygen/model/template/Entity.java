package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.model.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Entity {
    private String className;

    private String tableName;
    private String schema;
    private String catalog;

    @Builder.Default
    private List<Field> fields = new ArrayList<>();
    @Builder.Default
    private List<Relation> relations = new ArrayList<>();
    @Builder.Default
    private List<UniqueConstraint> uniqueConstraints = new ArrayList<>();

    private boolean hasCompositeKey;
    private EmbeddedId embeddedId;

    public List<Field> getPrimaryKeyFields() {
        return fields.stream().filter(Field::isPrimaryKey).toList();
    }
}
