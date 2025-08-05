package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TemplateUtil {

    public static String getComponentName(String entityName, ComponentType componentType, UserConfig userConfig) {
        return String.format(TemplateConst.COMPONENT_NAME, entityName, getComponentSuffix(componentType, userConfig));
    }

    public static String getComponentImport(ComponentType componentType, UserConfig userConfig, String entityName) {
        String template = TemplateConst.SIMPLE_IMPORT;
        return switch (componentType) {
            case EMBEDDABLE -> String.format(template, userConfig.getEmbeddablePackage(), entityName);
            case ENTITY -> String.format(template, userConfig.getEntityPackage(), entityName + userConfig.getEntitySuffix());
            case DTO -> String.format(template, userConfig.getDtoPackage(), entityName + userConfig.getDtoSuffix());
            case REPOSITORY -> String.format(template, userConfig.getRepositoryPackage(), entityName + userConfig.getRepositorySuffix());
            case SERVICE -> String.format(template, userConfig.getServicePackage(), entityName + userConfig.getServiceSuffix());
            case SERVICE_IMPL -> String.format(template, userConfig.getServiceImplPackage(), entityName + userConfig.getServiceImplSuffix());
            case CONTROLLER ->  String.format(template, userConfig.getControllerPackage(), entityName + userConfig.getControllerSuffix());
        };
    }

    public static String getComponentPackagePath(ComponentType componentType, UserConfig userConfig) {
        return resolvePath(getComponentPackage(componentType, userConfig), userConfig);
    }

    public static String getComponentPackage(ComponentType componentType, UserConfig userConfig) {
        return switch (componentType) {
            case ENTITY -> userConfig.getEntityPackage();
            case EMBEDDABLE -> userConfig.getEmbeddablePackage();
            case DTO -> userConfig.getDtoPackage();
            case REPOSITORY -> userConfig.getRepositoryPackage();
            case SERVICE -> userConfig.getServicePackage();
            case SERVICE_IMPL -> userConfig.getServiceImplPackage();
            case CONTROLLER -> userConfig.getControllerPackage();
        };
    }

    private static String resolvePath(String componentPackage, UserConfig userConfig) {
        return Paths.get(userConfig.getOutputDirectory())
                .resolve(componentPackage.replace(".", File.separator))
                .toString();
    }

    public static String getComponentSuffix(ComponentType componentType, UserConfig userConfig) {
        return switch (componentType) {
            case ENTITY -> userConfig.getEntitySuffix();
            case EMBEDDABLE -> userConfig.getEmbeddableSuffix();
            case DTO -> userConfig.getDtoSuffix();
            case REPOSITORY -> userConfig.getRepositorySuffix();
            case SERVICE -> userConfig.getServiceSuffix();
            case SERVICE_IMPL -> userConfig.getServiceImplSuffix();
            case CONTROLLER -> userConfig.getControllerSuffix();
        };
    }

    public static String getEntityIdType(Entity entity) {
        if (entity.isCompositeKey() && entity.getEmbeddedId() != null) {
            return entity.getEmbeddedId().getClassName();
        } else {
            return entity.getPrimaryKeyFields().getFirst().getJavaType();
        }
    }

    public static String getEntityIdFieldName(Entity entity) {
        if (entity == null) return null;

        if (entity.isCompositeKey()) {
            return entity.getEmbeddedId().getClassName();
        } else {
            return entity.getPrimaryKeyFields().getFirst().getName();
        }
    }

    public static String joinParams(String delimiter, String ...params) {
        return Arrays.stream(params)
                .filter(param -> param != null && !param.isEmpty())
                .collect(Collectors.joining(delimiter));
    }

    public static String formatOptionalParameters(String... params) {
        if (params == null || params.length == 0) {
            return "";
        }

        String formatted = String.join(", ", params);
        return formatted.isEmpty() ? "" : "(" + formatted + ")";
    }


}
