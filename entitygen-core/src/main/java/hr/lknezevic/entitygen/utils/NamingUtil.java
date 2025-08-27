package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.RelationType;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for naming conventions and transformations.
 * Provides methods to convert strings to PascalCase, camelCase, and generate field names based on entity relationships.
 */
public class NamingUtil {

    private NamingUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Converts a given string to PascalCase.
     *
     * @param input the string to convert
     * @return the converted string in PascalCase
     */
    public static String toPascalCase(String input) {
        if (StringUtils.isEmpty(input)) return input;

        StringBuilder sb = new StringBuilder();
        if (input.contains("_")) {
            input = input.trim().replaceAll("_+", "_");

            String[] parts = input.split("_");
            for (String part : parts) {
                if (part.isEmpty()) continue;
                sb.append(Character.toUpperCase(part.charAt(0)));
                if (part.length() > 1) {
                    sb.append(part.substring(1).toLowerCase());
                }
            }

            return sb.toString();
        }
        
        return sb.append(Character.toUpperCase(input.charAt(0)))
                .append(input.substring(1).toLowerCase())
                .toString();
    }

    /**
     * Converts a given string to camelCase.
     *
     * @param input the string to convert
     * @return the converted string in camelCase
     */
    public static String toCamelCase(String input) {
        String pascal = toPascalCase(input);
        if (pascal.isEmpty()) return pascal;
        return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
    }

    /**
     * Generates a field name based on the target entity class, relation type, and whether it is a collection.
     *
     * @param targetEntityClass the class name of the target entity
     * @param relationType the type of relation
     * @param isCollection whether the field represents a collection
     * @return the generated field name
     */
    public static String generateFieldName(String targetEntityClass, RelationType relationType, boolean isCollection) {
        String baseName = toCamelCase(targetEntityClass);
        
        if (!isCollection) {
            return baseName;
        }

        if (relationType == RelationType.MANY_TO_MANY) {
            return baseName + "Set";
        }

        return baseName + "List";
    }

    /**
     * Generates a self-referencing field name based on the target entity class, relation type, and whether it is inverse.
     *
     * @param targetEntityClass the class name of the target entity
     * @param relationType the type of relation
     * @param isInverse whether the field is inverse
     * @return the generated self-referencing field name
     */
    public static String generateSelfRefFieldName(String targetEntityClass, RelationType relationType, boolean isInverse) {
        return switch (relationType) {
            case MANY_TO_ONE -> {
                if (isInverse) {
                    yield "parent" + NamingUtil.toPascalCase(targetEntityClass);
                } else {
                    yield NamingUtil.generateFieldName(targetEntityClass, relationType, false);
                }
            }
            case ONE_TO_ONE -> {
                if (isInverse) {
                    yield "related" + NamingUtil.toPascalCase(targetEntityClass);
                } else {
                    yield NamingUtil.generateFieldName(targetEntityClass, relationType, false);
                }
            }
            case ONE_TO_MANY -> {
                if (isInverse) {
                    yield "children";
                } else {
                    yield NamingUtil.generateFieldName(targetEntityClass, relationType, true);
                }
            }
            default -> NamingUtil.generateFieldName(targetEntityClass, relationType, isInverse);
        };
    }

    /**
     * Generates an embeddable class name based on the table name and user configuration.
     *
     * @param tableName the name of the table
     * @param userConfig the user configuration containing the embeddable suffix
     * @return the generated embeddable class name
     */
    public static String generateEmbeddableClassName(String tableName, UserConfig userConfig) {
        return toPascalCase(tableName) + userConfig.getEmbeddableSuffix();
    }

    /**
     * Capitalizes the first letter of a given string.
     *
     * @param str the string to capitalize
     * @return string with the first letter capitalized
     */
    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Uncapitalizes the first letter of a given string.
     *
     * @param str the string to uncapitalize
     * @return string with the first letter uncapitalized
     */
    public static String uncapitalize(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
