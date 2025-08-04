package hr.lknezevic.entitygen.mapper;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.EmbeddedId;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Field;
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

            Set<String> foreignKeyColumns = table.getForeignKeys().stream()
                    .map(ForeignKey::getFkColumn)
                    .collect(Collectors.toSet());

            List<Field> fields = table.getColumns().stream()
                    .filter(column -> !foreignKeyColumns.contains(column.getName()) || column.isPrimaryKey())
                    .map(this::mapColumnToField)
                    .toList();

            boolean hasCompositeKey = checkCompositeKey(getAllFieldsIncludingFK(table));
            EmbeddedId embeddedId = hasCompositeKey
                    ? buildEmbeddedId(table.getName(), getAllFieldsIncludingFK(table))
                    : null;

            Entity entity = Entity.builder()
                    .className(NamingUtil.toPascalCase(table.getName()))
                    .tableName(table.getName())
                    .schema(table.getSchema())
                    .catalog(table.getCatalog())
                    .fields(fields)
                    .compositeKey(hasCompositeKey)
                    .embeddedId(embeddedId)
                    .uniqueConstraints(table.getUniqueConstraints())
                    .build();

            entities.add(entity);
        }

        return entities;
    }

    private List<Field> getAllFieldsIncludingFK(Table table) {
        return table.getColumns().stream()
                .map(this::mapColumnToField)
                .toList();
    }

    private Field mapColumnToField(Column column) {
        return Field.builder()
                .name(NamingUtil.toCamelCase(column.getName()))
                .columnName(column.getName())
                .javaType(resolveJavaTypeForId(column.getDataType(), column.getJavaType()))
                .length(column.getLength())
                .precision(column.getPrecision())
                .scale(column.getScale())
                .primaryKey(column.isPrimaryKey())
                .nullable(column.isNullable())
                .unique(column.isUnique())
                .autoIncrement(column.isAutoIncrement())
                .lob(column.isLob())
                .defaultValue(column.getDefaultValue())
                .comment(column.getComment())
                .build();
    }

    private EmbeddedId buildEmbeddedId(String tableName, List<Field> fields) {
        List<Field> pkFields = fields.stream()
                .filter(Field::isPrimaryKey)
                .toList();

        return EmbeddedId.builder()
                .className(NamingUtil.generateEmbeddableClassName(tableName, userConfig))
                .fields(pkFields)
                .build();
    }

    private String resolveJavaTypeForId(Integer dataType, String currentIdType) {
        if (userConfig.getPreferLongIds() && dataType.equals(Types.INTEGER)) return "Long";

        return currentIdType;
    }

}
