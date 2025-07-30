package hr.lknezevic.entitygen.helper;

import hr.lknezevic.entitygen.enums.RelationType;

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
     * Generiraj pametan naziv polja za relaciju na temelju naziva target entiteta i relation type
     * Ovo je glavna metoda za generiranje field name-a
     */
    public static String generateFieldName(String targetEntityClass, String relationType, boolean isCollection) {
        String baseName = toCamelCase(targetEntityClass);
        
        if (!isCollection) {
            return baseName;
        }
        
        // Za kolekcije, koristi pametan suffix ovisno o relation type
        return switch (relationType.toUpperCase()) {
            case "MANY_TO_MANY" -> baseName + "Set";        // SET je bolji za M2M zbog unique nature
            case "MANY_TO_MANY_DIRECT" -> baseName + "Set"; // Direktni M2M također koristi SET
            case "ONE_TO_MANY" -> baseName + "List";        // LIST je standard za 1:M
            default -> baseName + "List";                   // Default fallback
        };
    }
    
    /**
     * Generiraj naziv polja za relaciju na temelju RelationType enuma (type-safe verzija)
     */
    public static String generateFieldName(String targetEntityClass, RelationType relationType, boolean isCollection) {
        return generateFieldName(targetEntityClass, relationType.name(), isCollection);
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
