package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.utils.NamingUtil;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class TemplateModel {
    protected final ComponentType componentType;
    protected final Entity entity;
    protected final UserConfig config;
    protected final List<String> imports;
    protected final Map<String, Entity> entityByClassName;

    abstract List<String> getAllImports();

    protected String getName() {
        return entity.getClassName() + getComponentSuffix(componentType);
    }

    protected String getPackage() {
        return getComponentPackage(componentType);
    }

    protected String getComponentSuffix(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY -> config.getEntitySuffix();
            // case EMBEDDABLE -> config.getEmbeddableSuffix();
            case DTO -> config.getDtoSuffix();
            case REPOSITORY -> config.getRepositorySuffix();
            case SERVICE -> config.getServiceSuffix();
            case SERVICE_IMPL -> config.getServiceSuffix() + "Impl";
            case CONTROLLER -> config.getControllerSuffix();
            default -> "";
        };
    }

    protected String getComponentPackagePath() {
        return resolvePath(getComponentPackage(componentType));
    }

    protected String capitalize(String str) {
        return NamingUtil.capitalize(str);
    }

    private String getComponentPackage(ComponentType componentType) {
        return switch (componentType) {
            case ENTITY, EMBEDDABLE -> config.getEntityPackage();
            case DTO -> config.getDtoPackage();
            case REPOSITORY -> config.getRepositoryPackage();
            case SERVICE -> config.getServicePackage();
            case SERVICE_IMPL -> config.getServicePackage() + ".impl";
            case CONTROLLER -> config.getControllerPackage();
        };
    }

    private String resolvePath(String componentPackage) {
        return Paths.get(config.getOutputDirectory())
                .resolve(componentPackage.replace(".", File.separator))
                .toString();
    }

}
