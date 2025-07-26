package hr.lknezevic.entitygen.helper;

/**
 * Helper klasa za imenovanje polja i klasa u relacijama
 */
public class NamingHelper {
    
    /**
     * Konvertira ime tablice u PascalCase (ClassName)
     */
    public static String toPascalCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String[] parts = input.split("_");
        StringBuilder sb = new StringBuilder();
        
        for (String part : parts) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                sb.append(part.substring(1).toLowerCase());
            }
        }
        
        return sb.toString();
    }
    
    /**
     * Konvertira ime tablice u camelCase (fieldName)
     */
    public static String toCamelCase(String input) {
        String pascal = toPascalCase(input);
        if (pascal.isEmpty()) {
            return pascal;
        }
        return Character.toLowerCase(pascal.charAt(0)) + pascal.substring(1);
    }
    
    /**
     * Generiraj naziv polja za relaciju na temelju naziva target entiteta
     */
    public static String generateFieldName(String targetEntityClass, String relationType, boolean isCollection) {
        String baseName = toCamelCase(targetEntityClass);
        
        if (isCollection) {
            // Za kolekcije dodaj suffix
            return baseName + "List";
        }
        
        return baseName;
    }
    
    /**
     * Generiraj naziv polja za inverznu relaciju
     */
    public static String generateInverseFieldName(String sourceEntityClass, String targetEntityClass, String relationType) {
        String baseName = toCamelCase(sourceEntityClass);
        
        // Za ONE_TO_MANY uvijek je lista
        if ("ONE_TO_MANY".equals(relationType)) {
            return baseName + "List";
        }
        
        return baseName;
    }
    
    /**
     * Ukloni prefiks/suffix iz imena tablice ako je potrebno
     */
    public static String cleanTableName(String tableName) {
        if (tableName == null || tableName.isEmpty()) {
            return tableName;
        }
        
        // Ukloni uobičajene prefikse kao što su "tbl_", "t_"
        String cleaned = tableName;
        if (cleaned.startsWith("tbl_")) {
            cleaned = cleaned.substring(4);
        } else if (cleaned.startsWith("t_")) {
            cleaned = cleaned.substring(2);
        }
        
        return cleaned;
    }
    
    /**
     * Generiraj naziv za embeddable klasu
     */
    public static String generateEmbeddableClassName(String tableName) {
        return toPascalCase(cleanTableName(tableName)) + "Id";
    }
}
