package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Entity;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TemplateUtil {

    public static Boolean shouldGenerate(ComponentType componentType, UserConfig userConfig) {
        if (userConfig.getGenerateAllComponents()) return  true;

        return componentType == ComponentType.ENTITY || componentType == ComponentType.EMBEDDABLE;
    }

    public static boolean overwriteComponent(ComponentType componentType, File file, UserConfig userConfig) {
        return !userConfig.getOverwriteExisting() && file.exists();
    }

    public static String getComponentName(String entityName, ComponentType componentType, UserConfig userConfig) {
        return TemplateFactory.builder()
                .template(TemplateConst.COMPONENT_NAME)
                .build()
                .addParams(entityName, getComponentSuffix(componentType, userConfig))
                .format();
    }

    public static String getComponentImport(ComponentType componentType, UserConfig userConfig, String entityName) {
        TemplateFactory factory = TemplateFactory.builder()
                .template(TemplateConst.SIMPLE_IMPORT)
                .build();

        return switch (componentType) {
            case EMBEDDABLE -> factory.addParams(userConfig.getEmbeddablePackage(), entityName).format();
            case ENTITY -> factory.addParams(userConfig.getEntityPackage(), entityName + userConfig.getEntitySuffix()).format();
            case DTO -> factory.addParams(userConfig.getDtoPackage(), entityName + userConfig.getDtoSuffix()).format();
            case REPOSITORY -> factory.addParams(userConfig.getRepositoryPackage(), entityName + userConfig.getRepositorySuffix()).format();
            case SERVICE -> factory.addParams(userConfig.getServicePackage(), entityName + userConfig.getServiceSuffix()).format();
            case SERVICE_IMPL -> factory.addParams(userConfig.getServiceImplPackage(), entityName + userConfig.getServiceImplSuffix()).format();
            case CONTROLLER -> factory.addParams(userConfig.getControllerPackage(), entityName + userConfig.getControllerSuffix()).format();
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
                .filter(param -> !StringUtils.isBlank(param))
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
