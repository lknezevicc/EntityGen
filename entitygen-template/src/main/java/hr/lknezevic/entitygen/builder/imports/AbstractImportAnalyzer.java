package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.common.Entity;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class AbstractImportAnalyzer implements ImportAnalyzer {
    protected final Set<TemplateImport> imports = new HashSet<>();
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

    protected boolean hasAdditionalImports() {
        return false;
    }

    protected void analyzeFields() {
        if (entity.getFields().isEmpty()) return;

        entity.getFields().forEach(field -> {
            TemplateImport typeImport = findImportForJavaType(field.getJavaType());
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
                    case LIST -> imports.add(TemplateImport.JAVA_LIST);
                    case SET -> imports.add(TemplateImport.JAVA_SET);
                }
            }
        });
    }

    private List<String> sortImports() {
        if (imports.isEmpty()) return new ArrayList<>();

        Map<Integer, List<TemplateImport>> map = imports.stream()
                .collect(Collectors.groupingBy(
                        TemplateImport::getOrder,
                        TreeMap::new,
                        Collectors.toList()
                ));

        List<String> result = new ArrayList<>();
        List<Integer> keys = new ArrayList<>(map.keySet());

        for (int i = 0; i < keys.size(); i++) {
            Integer orderKey = keys.get(i);
            List<TemplateImport> group = map.get(orderKey);

            List<String> sorted = group.stream()
                    .sorted(Comparator.comparing(TemplateImport::getValue))
                    .map(TemplateImport::getValue)
                    .toList();

            result.addAll(sorted);

//            if (i < keys.size() - 1) {
//                result.add("");
//            }

            result.add("");
        }

        return result;
    }

    private List<String> sortOtherImports() {
        if (otherImports.isEmpty()) return new ArrayList<>();
        return otherImports.stream().sorted(String::compareTo).toList();
    }

    private TemplateImport findImportForJavaType(String javaType) {
        return switch (javaType) {
            case "BigDecimal" -> TemplateImport.JAVA_BIG_DECIMAL;
            case "BigInteger" -> TemplateImport.JAVA_BIG_INTEGER;
            case "Date" -> TemplateImport.JAVA_DATE;
            case "LocalDate" -> TemplateImport.JAVA_LOCAL_DATE;
            case "LocalTime" -> TemplateImport.JAVA_LOCAL_TIME;
            case "LocalDateTime" -> TemplateImport.JAVA_LOCAL_DATE_TIME;
            default -> null;
        };
    }

}
