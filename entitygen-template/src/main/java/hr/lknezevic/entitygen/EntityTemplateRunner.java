package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.analyzer.RelationBuilder;
import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.helper.TemplateRunnerHelper;
import hr.lknezevic.entitygen.mapper.DefaultEntityModelMapper;
import hr.lknezevic.entitygen.mapper.EntityModelMapper;
import hr.lknezevic.entitygen.model.RelationContext;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.model.domain.Relation;

import java.util.List;

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
 */
public class EntityTemplateRunner {
    private final List<Table> tables;
    private final TemplateRunnerHelper templateRunnerHelper;
    private final EntityModelMapper entityModelMapper;

    public EntityTemplateRunner(List<Table> tables, UserConfig userConfig) {
        this.tables = tables;
        this.templateRunnerHelper = new TemplateRunnerHelper(userConfig);
        this.entityModelMapper = new DefaultEntityModelMapper(userConfig);
    }

    /**
     * Generates all configured components for the provided tables.
     * Generated components depend on {@link UserConfig} settings.
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

        templateRunnerHelper.setEntityByClassName(relationContext.getEntityByTableName().values().stream().toList());
        relationContext.getAllEntities().forEach(templateRunnerHelper::generateComponents);
    }
}