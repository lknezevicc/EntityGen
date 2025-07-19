package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.model.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Entity {
    private String className;
    private String packageName;

    private String tableName;
    private String schema;
    private String catalog;

    private List<Field> fields;
    private boolean hasCompositeKey;
    private EmbeddedId embeddedId;

    private List<Relation> relations;
    private List<UniqueConstraint> uniqueConstraints;
}
