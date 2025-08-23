package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.Imports;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

/**
 * Analyzes and provides the necessary imports for a Spring Service class.
 */
public class ServiceImportAnalyzer extends AbstractImportAnalyzer {

    public ServiceImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Service class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                Imports.JAVA_GENERATED,
                Imports.JAVA_LIST
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.DTO, userConfig, entity.getClassName()));
    }

}
