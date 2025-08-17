package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.Imports;
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
                Imports.JAVA_GENERATED,
                Imports.LOMBOK_DATA,
                Imports.LOMBOK_NO_ARGS_CONSTRUCTOR,
                Imports.LOMBOK_ALL_ARGS_CONSTRUCTOR,
                Imports.LOMBOK_BUILDER,
                Imports.JPA_EMBEDDABLE,
                Imports.JPA_COLUMN,
                Imports.JAVA_SERIALIZABLE,
                Imports.JAVA_SERIAL
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

        imports.add(Imports.JPA_COLUMN);

        entity.getPrimaryKeyFields().forEach(field -> {
            Imports typeImport = findImportForJavaType(field.getJavaType());
            if (typeImport != null) {
                imports.add(typeImport);
            }
        });
    }

}
