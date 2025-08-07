package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.common.Relation;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for handling template-related operations.
 * Provides methods to determine if a component should be generated,
 * manage component names, imports, and package paths.
 */
public class TemplateUtil {

    /**
     * Determines if a component should be generated based on its type and user configuration.
     *
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @return true if the component should be generated, false otherwise
     */
    public static Boolean shouldGenerate(ComponentType componentType, UserConfig userConfig) {
        if (userConfig.getGenerateAllComponents()) return true;

        return componentType == ComponentType.ENTITY || componentType == ComponentType.EMBEDDABLE;
    }

    /**
     * Checks if a component file should be overwritten based on user configuration.
     *
     * @param file the file to check
     * @param userConfig the user configuration
     * @return true if the file should not be overwritten, false otherwise
     */
    public static boolean overwriteComponent(File file, UserConfig userConfig) {
        return userConfig.getOverwriteExisting() || !file.exists();
    }

    /**
     * Generates the component name based on the entity name, component type, and user configuration.
     *
     * @param entityName the name of the entity
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @return the formatted component name
     */
    public static String getComponentName(String entityName, ComponentType componentType, UserConfig userConfig) {
        return TemplateFactory.builder()
                .template(TemplateConst.COMPONENT_NAME)
                .build()
                .addParams(entityName, getComponentSuffix(componentType, userConfig))
                .format();
    }

    /**
     * Generates the import statement for a component based on its type, user configuration, and entity name.
     *
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @param entityName the name of the entity
     * @return the formatted import statement
     */
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

    /**
     * Resolves the package path for a component based on its type and user configuration.
     *
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @return the resolved package path as a string
     */
    public static String getComponentPackagePath(ComponentType componentType, UserConfig userConfig) {
        return resolvePath(getComponentPackage(componentType, userConfig), userConfig);
    }

    /**
     * Retrieves the package name for a component based on its type and user configuration.
     *
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @return the package name as a string
     */
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

    /**
     * Retrieves the suffix for a component based on its type and user configuration.
     *
     * @param componentType the type of the component
     * @param userConfig the user configuration
     * @return the suffix as a string
     */
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

    /**
     * Retrieves the ID type of entity, considering whether it has a composite key or not.
     *
     * @param entity the entity for which to retrieve the ID type
     * @return the ID type as a string
     */
    public static String getEntityIdType(Entity entity) {
        if (entity.isCompositeKey() && entity.getEmbeddedId() != null) {
            return entity.getEmbeddedId().getClassName();
        } else {
            return entity.getPrimaryKeyFields().getFirst().getJavaType();
        }
    }

    /**
     * Retrieves the field name for the entity ID, considering whether it has a composite key or not.
     *
     * @param entity the entity for which to retrieve the ID field name
     * @return the ID field name as a string
     */
    public static String getEntityIdFieldName(Entity entity) {
        if (entity == null) return null;

        if (entity.isCompositeKey()) {
            return entity.getEmbeddedId().getClassName();
        } else {
            return entity.getPrimaryKeyFields().getFirst().getName();
        }
    }

    /**
     * Builds the MapsId annotation for a relation, if applicable.
     *
     * @param relation the relation for which to build the MapsId annotation
     * @return the formatted MapsId annotation as a string, or an empty string if not applicable
     */
    public static String buildMapsId(Relation relation) {
        if (relation.getMapsId() == null) return "";

        if (relation.getMapsId().isEmpty()) {
            return TemplateConst.MAPS_ID;
        } else {
            return TemplateFactory.builder()
                    .template(TemplateConst.MAPS_ID_WITH_VALUE)
                    .build()
                    .addParam(relation.getMapsId())
                    .format();
        }
    }

    /**
     * Builds a single JoinColumn annotation for a relation.
     *
     * @param columnName the name of the column
     * @param referencedColumn the name of the referenced column
     * @param additionalParams additional parameters to include in the annotation
     * @return the formatted JoinColumn annotation as a string
     */
    public static String buildJoinColumn(String columnName, String referencedColumn, List<String> additionalParams) {
        String additionalParamsStr = additionalParams.isEmpty() ? "" : ", " + String.join(", ", additionalParams);

        return TemplateFactory.builder()
                .template(TemplateConst.JOIN_COLUMN_SINGLE)
                .build()
                .addParam(columnName)
                .addParam(referencedColumn)
                .addParam(additionalParamsStr)
                .format();
    }

    /**
     * Builds JoinColumns annotations for a relation, handling both single and composite keys.
     *
     * @param columns the list of column names
     * @param referencedColumns the list of referenced column names
     * @param hasCompositeKey whether the relation has a composite key
     * @param additionalParams additional parameters to include in the annotations
     * @return the formatted JoinColumns annotations as a string
     */
    public static String buildJoinColumns(List<String> columns, List<String> referencedColumns,
                                          boolean hasCompositeKey, List<String> additionalParams) {
        if (columns.isEmpty()) return "";

        if (hasCompositeKey) {
            additionalParams.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_UPDATABLE_INSERTABLE)
                    .build()
                    .addParams(false, false)
                    .format()
            );
        }

        if (columns.size() == 1) {
            String referencedColumn = referencedColumns.isEmpty() ?
                    TemplateConst.DEFAULT_REFERENCED_COLUMN : referencedColumns.getFirst();

            return buildJoinColumn(columns.getFirst(), referencedColumn, additionalParams);
        } else {
            List<String> items = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                String referencedColumn = i < referencedColumns.size() ?
                        referencedColumns.get(i) : TemplateConst.DEFAULT_REFERENCED_COLUMN;

                items.add(buildJoinColumn(columns.get(i), referencedColumn, additionalParams));
            }

            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMNS_MULTIPLE)
                    .build()
                    .addParam(TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE, items.toArray(new String[0])))
                    .format();
        }
    }

    /**
     * Builds the columns for a join table, handling both single and multiple columns.
     *
     * @param columns the list of column names
     * @param referencedColumns the list of referenced column names
     * @return the formatted join table columns as a string
     */
    public static String buildJoinTableColumns(List<String> columns, List<String> referencedColumns) {
        if (columns.isEmpty()) return "";

        if (columns.size() == 1) {
            String referencedColumn = referencedColumns.isEmpty() ?
                    TemplateConst.DEFAULT_REFERENCED_COLUMN : referencedColumns.getFirst();

            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMN_SINGLE)
                    .build()
                    .addParam(columns.getFirst())
                    .addParam(referencedColumn)
                    .addParam("")
                    .format();
        } else {
            List<String> items = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                String referencedColumn = i < referencedColumns.size() ?
                        referencedColumns.get(i) : TemplateConst.DEFAULT_REFERENCED_COLUMN;

                items.add(TemplateFactory.builder()
                        .template(TemplateConst.JOIN_COLUMN_SINGLE)
                        .build()
                        .addParam(columns.get(i))
                        .addParam(referencedColumn)
                        .addParam("")
                        .format());
            }

            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMNS_MULTIPLE)
                    .build()
                    .addParam(TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE, items.toArray(new String[0])))
                    .format();
        }
    }

    /**
     * Builds the JoinTable annotation for a relation, including join columns and inverse join columns.
     *
     * @param relation the relation for which to build the JoinTable annotation
     * @return the formatted JoinTable annotation as a string, or an empty string if no join table is defined
     */
    public static String buildJoinTable(Relation relation) {
        if (relation.getJoinTableName() == null) return "";

        String joinColumns = buildJoinTableColumns(relation.getJoinColumns(), relation.getReferencedColumns());

        String inverseJoinColumns = buildJoinTableColumns(relation.getInverseJoinColumns(), relation.getInverseReferencedColumns());

        return TemplateFactory.builder()
                .template(TemplateConst.JOIN_TABLE)
                .build()
                .addParam(relation.getJoinTableName())
                .addParam(joinColumns)
                .addParam(inverseJoinColumns)
                .format();
    }

    /**
     * Builds the parameters for a column based on the field properties.
     *
     * @param field the field for which to build the column parameters
     * @return the formatted column parameters as a string
     */
    public static String buildColumnParams(Field field) {
        List<String> params = new ArrayList<>();

        params.add(
                TemplateFactory.builder()
                        .template(TemplateConst.COLUMN_PARAM_NAME)
                        .build().addParam(field.getColumnName())
                        .format()
        );

        if (!field.isNullable()) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_NULLABLE)
                            .build().addParam(false)
                            .format()
            );
        }

        if (field.isUnique()) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_UNIQUE)
                            .build().addParam(true)
                            .format()
            );
        }

        if (!field.isLob() && field.getLength() != null && field.getLength() <= 255) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_LENGTH)
                            .build().addParam(field.getLength())
                            .format()
            );
        }

        if (!field.isLob() && field.getLength() != null && field.getLength() == 65535) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_LENGTH_LARGE)
                            .build()
                            .format()
            );
        }

        if (field.getPrecision() != null && field.getPrecision() > 0) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_PRECISION)
                            .build()
                            .addParam(field.getPrecision())
                            .format()
            );
        }

        if (field.getScale() != null && field.getScale() > 0) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_SCALE)
                            .build()
                            .addParam(field.getScale())
                            .format()
            );
        }

        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN, params.toArray(new String[0]));
    }

    /**
     * Joins parameters into a single string with the specified delimiter, filtering out blank values.
     *
     * @param delimiter the delimiter to use for joining
     * @param params the parameters to join
     * @return the joined string
     */
    public static String joinParams(String delimiter, String ...params) {
        return Arrays.stream(params)
                .filter(param -> !StringUtils.isBlank(param))
                .collect(Collectors.joining(delimiter));
    }

    /**
     * Formats optional parameters into a string representation, enclosed in parentheses.
     *
     * @param params the parameters to format
     * @return the formatted string, or an empty string if no parameters are provided
     */
    public static String formatOptionalParameters(String... params) {
        if (params == null || params.length == 0) {
            return "";
        }

        String formatted = String.join(", ", params);
        return formatted.isEmpty() ? "" : "(" + formatted + ")";
    }

    private static String resolvePath(String componentPackage, UserConfig userConfig) {
        return Paths.get(userConfig.getOutputDirectory())
                .resolve(componentPackage.replace(".", File.separator))
                .toString();
    }

}
