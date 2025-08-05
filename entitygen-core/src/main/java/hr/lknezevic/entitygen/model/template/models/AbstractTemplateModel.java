package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.FieldModule;
import hr.lknezevic.entitygen.model.template.modules.ImportModule;
import hr.lknezevic.entitygen.model.template.modules.PrimaryKeyModule;
import hr.lknezevic.entitygen.model.template.modules.RelationModule;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Abstract class for template models that provides common functionality
 * for constructing entity-related templates.
 *
 * @author leonknezevic
 */
@RequiredArgsConstructor
public abstract class AbstractTemplateModel implements TemplateModel {
    protected final ComponentType componentType;
    protected final Entity entity;
    protected final UserConfig userConfig;
    protected final Map<String, Entity> entityByClassName;
    protected final List<String> imports;

    protected String getPrimaryKey() {
        return PrimaryKeyModule.builder()
                .componentType(componentType)
                .entity(entity)
                .build()
                .construct();
    }

    protected String getNonPrimaryFields() {
        if (entity.getNonPrimaryKeyFields().isEmpty()) return "";

        List<String> params = entity.getNonPrimaryKeyFields().stream()
                .map(field ->
                        FieldModule.builder()
                                .componentType(componentType)
                                .field(field)
                                .build()
                                .construct()
                ).toList();

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, params);
    }

    protected String getRelationFields() {
        if (entity.getRelations().isEmpty()) return "";

        List<String> params = entity.getRelations().stream()
                .map(relation -> {
                    Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());
                    if (targetEntity == null) return "";

                    return RelationModule.builder()
                            .componentType(componentType)
                            .sourceEntity(entity)
                            .relation(relation)
                            .targetEntity(targetEntity)
                            .build()
                            .construct();
                }).toList();

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, params);
    }

    @Override
    public String getComponentName() {
        return getName(componentType);
    }

    @Override
    public String getComponentPackage() {
        return getPackage(componentType);
    }

    @Override
    public String getEmbeddedIdName() {
        return getName(ComponentType.EMBEDDABLE);
    }

    @Override
    public String getEntityName() {
        return getName(ComponentType.ENTITY);
    }

    @Override
    public String getDtoName() {
        return getName(ComponentType.DTO);
    }

    @Override
    public String getRepositoryName() {
        return getName(ComponentType.REPOSITORY);
    }

    @Override
    public String getServiceName() {
        return getName(ComponentType.SERVICE);
    }

    @Override
    public String getServiceImplName() {
        return getName(ComponentType.SERVICE_IMPL);
    }

    @Override
    public String getControllerName() {
        return getName(ComponentType.CONTROLLER);
    }

    @Override
    public String getComponentPackagePath() {
        return TemplateUtil.getComponentPackagePath(componentType, userConfig);
    }

    @Override
    public List<String> getImports() {
        return imports.stream()
                .map(imp -> new ImportModule(imp).construct())
                .toList();
    }

    @Override
    public String getModelBody() {
        return "";
    }

    private String getName(ComponentType componentType) {
        return TemplateUtil.getComponentName(entity.getClassName(), componentType, userConfig);
    }

    private String getPackage(ComponentType componentType) {
        return TemplateUtil.getComponentPackage(componentType, userConfig);
    }

}
