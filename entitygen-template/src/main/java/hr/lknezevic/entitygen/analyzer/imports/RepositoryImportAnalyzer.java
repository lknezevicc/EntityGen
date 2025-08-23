package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.Imports;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

/**
 * Analyzes and provides the necessary imports for a Spring Data Repository class.
 */
public class RepositoryImportAnalyzer extends AbstractImportAnalyzer {

    public RepositoryImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Repository class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                Imports.JAVA_GENERATED,
                Imports.SPRING_REPOSITORY,
                Imports.SPRING_JPA_REPOSITORY
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.ENTITY, userConfig, entity.getClassName()));
        if (entity.isCompositeKey())
            otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, entity.getEmbeddedId().getClassName()));
    }

}
