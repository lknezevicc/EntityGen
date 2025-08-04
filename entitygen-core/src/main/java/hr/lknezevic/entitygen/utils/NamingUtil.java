package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.RelationType;

public class NamingUtil {

    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

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

    public static String toCamelCase(String input) {
        String pascal = toPascalCase(input);
        if (pascal.isEmpty()) {
            return pascal;
        }
        return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
    }

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

    public static String generateEmbeddableClassName(String tableName, UserConfig userConfig) {
        return toPascalCase(tableName) + userConfig.getEmbeddableSuffix();
    }

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String uncapitalize(String str) {
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }
}
