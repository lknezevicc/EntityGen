package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.Imports;
import hr.lknezevic.entitygen.model.template.common.Entity;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Abstract class for analyzing and collecting imports for an entity.
 * It provides methods to analyze fields, relations, and additional imports.
 */
@RequiredArgsConstructor
public abstract class AbstractImportAnalyzer implements ImportAnalyzer {
    protected final Set<Imports> imports = new HashSet<>();
    protected final Set<String> otherImports = new HashSet<>();
    protected final Entity entity;
    protected final UserConfig userConfig;
    protected final Map<String, Entity> entityByClassName;

    protected abstract void analyzeAdditionalImports();

    protected List<String> getCombinedImports() {
        List<String> combinedImports = new ArrayList<>();

        combinedImports.addAll(sortImports());
        combinedImports.addAll(sortOtherImports());

        return combinedImports;
    }

    protected void analyzeFields() {
        if (entity.getFields().isEmpty()) return;

        entity.getFields().forEach(field -> {
            Imports typeImport = findImportForJavaType(field.getJavaType());
            if (typeImport != null) {
                imports.add(typeImport);
            }
        });
    }

    protected void analyzeRelations() {
        if (entity.getRelations().isEmpty()) return;

        entity.getRelations().forEach(relation -> {
            if (relation.getCollectionType() != null) {
                switch (relation.getCollectionType()) {
                    case LIST -> imports.add(Imports.JAVA_LIST);
                    case SET -> imports.add(Imports.JAVA_SET);
                }
            }
        });
    }

    protected Imports findImportForJavaType(String javaType) {
        return switch (javaType) {
            case "BigDecimal" -> Imports.JAVA_BIG_DECIMAL;
            case "BigInteger" -> Imports.JAVA_BIG_INTEGER;
            case "Date" -> Imports.JAVA_DATE;
            case "LocalDate" -> Imports.JAVA_LOCAL_DATE;
            case "LocalTime" -> Imports.JAVA_LOCAL_TIME;
            case "LocalDateTime" -> Imports.JAVA_LOCAL_DATE_TIME;
            default -> null;
        };
    }

    private List<String> sortImports() {
        if (imports.isEmpty()) return new ArrayList<>();

        Map<Integer, List<Imports>> map = imports.stream()
                .collect(Collectors.groupingBy(
                        Imports::getOrder,
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<String> result = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(map.keySet());

        for (Integer orderKey : keys) {
            List<Imports> group = map.get(orderKey);

            List<String> sorted = group.stream()
                    .sorted(Comparator.comparing(Imports::getValue))
                    .map(Imports::getValue)
                    .toList();

            result.addAll(sorted);

            result.add("");
        }

        return result;
    }

    private List<String> sortOtherImports() {
        if (otherImports.isEmpty()) return new ArrayList<>();
        return otherImports.stream().sorted(String::compareTo).toList();
    }

}
