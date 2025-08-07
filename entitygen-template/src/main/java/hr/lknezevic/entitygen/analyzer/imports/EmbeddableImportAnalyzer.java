package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;

import java.util.List;
import java.util.Set;

/**
 * Analyzes and provides the necessary imports for an embeddable class in JPA.
 */
public class EmbeddableImportAnalyzer extends AbstractImportAnalyzer {

    public EmbeddableImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Embeddable class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.LOMBOK_DATA,
                TemplateImport.LOMBOK_NO_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_ALL_ARGS_CONSTRUCTOR,
                TemplateImport.LOMBOK_BUILDER,
                TemplateImport.JPA_EMBEDDABLE,
                TemplateImport.JPA_COLUMN,
                TemplateImport.JAVA_SERIALIZABLE,
                TemplateImport.JAVA_SERIAL
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        this.analyzeFields();
    }

    @Override
    protected void analyzeFields() {
        if (entity.getFields().isEmpty()) return;

        imports.add(TemplateImport.JPA_COLUMN);

        entity.getPrimaryKeyFields().forEach(field -> {
            TemplateImport typeImport = findImportForJavaType(field.getJavaType());
            if (typeImport != null) {
                imports.add(typeImport);
            }
        });
    }

}
