package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.helper.NamingHelper;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.EmbeddedId;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Field;
import lombok.RequiredArgsConstructor;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultEntityModelMapper implements EntityModelMapper {
    private final UserConfig userConfig;

    @Override
    public List<Entity> mapEntities(List<Table> tables) {
        List<Entity> entities = new ArrayList<>();

        for (Table table : tables) {
            // Get all foreign key column names for this table
            Set<String> foreignKeyColumns = table.getForeignKeys().stream()
                    .map(fk -> fk.getFkColumn())
                    .collect(Collectors.toSet());

            // Map columns to fields, but keep primary keys even if they are foreign keys
            List<Field> fields = table.getColumns().stream()
                    .filter(column -> !foreignKeyColumns.contains(column.getName()) || column.isPrimaryKey())
                    .map(this::mapColumnToField)
                    .toList();

            boolean hasCompositeKey = checkCompositeKey(getAllFieldsIncludingFK(table));
            EmbeddedId embeddedId = hasCompositeKey
                    ? buildEmbeddedId(table.getName(), getAllFieldsIncludingFK(table))
                    : null;

            Entity entity = Entity.builder()
                    .className(NamingHelper.toPascalCase(table.getName()))
                    .tableName(table.getName())
                    .schema(table.getSchema())
                    .catalog(table.getCatalog())
                    .fields(fields)
                    .hasCompositeKey(hasCompositeKey)
                    .embeddedId(embeddedId)
                    .uniqueConstraints(table.getUniqueConstraints())
                    .build();

            entities.add(entity);
        }

        return entities;
    }

    // Helper method to get all fields including FK for composite key checking
    private List<Field> getAllFieldsIncludingFK(Table table) {
        return table.getColumns().stream()
                .map(this::mapColumnToField)
                .toList();
    }

    private Field mapColumnToField(Column column) {
        return Field.builder()
                .name(NamingHelper.toCamelCase(column.getName()))
                .columnName(column.getName())
                .javaType(resolveJavaTypeForId(column.getDataType(), column.getJavaType()))
                .length(column.getLength())
                .precision(column.getPrecision())
                .scale(column.getScale())
                .primaryKey(column.isPrimaryKey())
                .nullable(column.isNullable())
                .unique(column.isUnique())
                .autoIncrement(column.isAutoIncrement())
                .generated(column.isGenerated())
                .isLob(column.isLob())
                .defaultValue(column.getDefaultValue())
                .comment(column.getComment())
                .build();
    }

    private EmbeddedId buildEmbeddedId(String tableName, List<Field> fields) {
        List<Field> pkFields = fields.stream()
                .filter(Field::isPrimaryKey)
                .toList();

        return EmbeddedId.builder()
                .className(NamingHelper.generateEmbeddableClassName(tableName))
                .fields(pkFields)
                .build();
    }

    private String resolveJavaTypeForId(Integer dataType, String currentIdType) {
        if (userConfig.getPreferLongIds() && dataType.equals(Types.INTEGER)) return "Long";

        return currentIdType;
    }

}
