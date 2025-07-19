package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.EmbeddedId;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Field;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultEntityModelMapper implements EntityModelMapper {

    @Override
    public List<Entity> mapEntities(List<Table> tables) {
        List<Entity> entities = new ArrayList<>();

        for (Table table : tables) {
            List<Field> fields = table.getColumns().stream()
                    .map(this::mapColumnToField)
                    .toList();

            boolean isComposite = checkCompositeKey(fields);
            EmbeddedId embeddedId = isComposite ?
                    buildEmbeddedId(table.getName(), fields) : null;

            List<Relation> relations = buildRelations(table.getForeignKeys());

            Entity entity = Entity.builder()
                    .className(table.getName()) // TODO implement PascalCase (NameUtils)
                    .tableName(table.getName())
                    .schema(table.getSchema())
                    .catalog(table.getCatalog())
                    .fields(fields)
                    .hasCompositeKey(isComposite)
                    .embeddedId(embeddedId)
                    .relations(relations)
                    .uniqueConstraints(table.getUniqueConstraints())
                    .build();

            entities.add(entity);
        }


        return entities;
    }

    private Field mapColumnToField(Column column) {
        return Field.builder()
                .name(column.getName())
                .columnName(column.getName())
                .javaType(column.getJavaType())
                .length(column.getLength())
                .precision(column.getPrecision())
                .scale(column.getScale())
                .primaryKey(column.isPrimaryKey())
                .nullable(column.isNullable())
                .unique(column.isUnique())
                .autoIncrement(column.isAutoIncrement())
                .generated(column.isGenerated())
                .defaultValue(column.getDefaultValue())
                .comment(column.getComment())
                .build();
    }

    private boolean checkCompositeKey(List<Field> fields) {
        return fields.stream().filter(Field::isPrimaryKey).count() > 1;
    }

    private EmbeddedId buildEmbeddedId(String className, List<Field> fields) {
        List<Field> pkFields = fields.stream()
                .filter(Field::isPrimaryKey)
                .toList();

        return EmbeddedId.builder()
                .className(className + "Id")
                .fields(pkFields)
                .build();
    }

    private List<Relation> buildRelations(List<ForeignKey> fks) {
        // implementirati kasnije (ili odmah ako imaš ForeignKey model)
        return Collections.emptyList();
    }

}
