package hr.lknezevic.entitygen.model.template.common;

import hr.lknezevic.entitygen.model.UniqueConstraint;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an entity in the context of a database.
 * Contains metadata about the entity such as its class name, table name,
 * schema, catalog, fields, relations, unique constraints, and whether it has a composite key or embedded ID.
 */
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

    private boolean compositeKey;
    private EmbeddedId embeddedId;

    /**
     * Returns a list of fields that are primary keys in this entity.
     *
     * @return a list of primary key field names
     */
    public List<Field> getPrimaryKeyFields() {
        return fields.stream().filter(Field::isPrimaryKey).toList();
    }

    /**
     * Returns a list of fields that are not primary keys in this entity.
     *
     * @return a list of non-primary key field names
     */
    public List<Field> getNonPrimaryKeyFields() {
        return fields.stream().filter(field -> !field.isPrimaryKey()).toList();
    }
}
