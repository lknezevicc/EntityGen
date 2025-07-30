package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.builder.RelationBuilder;
import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.helper.RelationContext;
import hr.lknezevic.entitygen.helper.TemplateRunnerHelper;
import hr.lknezevic.entitygen.mapper.DefaultEntityModelMapper;
import hr.lknezevic.entitygen.mapper.EntityModelMapper;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for generating JPA entities and related classes from database tables.
 * <p>
 * This class orchestrates the entire code generation process:
 * <ul>
 *   <li>Converts database tables to entity models</li>
 *   <li>Builds JPA relationships from foreign keys</li>
 *   <li>Generates Java files using FreeMarker templates</li>
 * </ul>
 * <p>
 * 
 * @author Leon Knežević
 */
@Slf4j
public class EntityTemplateRunner {
    private final List<Table> tables;
    private final TemplateRunnerHelper templateRunnerHelper;
    private final EntityModelMapper entityModelMapper;

    public EntityTemplateRunner(@NonNull List<Table> tables, @NonNull UserConfig userConfig) {
        this.tables = tables;
        this.templateRunnerHelper = new TemplateRunnerHelper(userConfig);
        this.entityModelMapper = new DefaultEntityModelMapper(userConfig);
    }

    /**
     * Generates all configured components for the provided tables.
     * Generated components depend on {@link UserConfig} settings. By default, only entities
     * and repositories are generated.
     */
    public void generateComponents() {
        List<Entity> entities = entityModelMapper.mapEntities(tables);
        RelationContext relationContext = new RelationContext(tables, entities);
        RelationBuilder relationBuilder = new RelationBuilder(relationContext);

        for (Table table : tables) {
            List<Relation> relations = relationBuilder.forTable(table).buildRelations();

            relationContext.getEntityByTableName()
                    .get(table.getName())
                    .setRelations(relations);
        }

        Map<String, Entity> entityByClassName = new HashMap<>();
        entityByClassName.putAll(relationContext.getEntityByTableName().values().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Entity::getClassName,
                        e -> e,
                        (existing, replacement) -> existing
                )));


        log.info("Entiteti {}", relationContext.getAllEntities());

        for (Entity entity : relationContext.getAllEntities()) {
            templateRunnerHelper.generateComponents(entity, entityByClassName);
        }
    }
}