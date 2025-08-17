package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.Imports;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

/**
 * Analyzes and provides the necessary imports for a Spring Controller class.
 */
public class ControllerImportAnalyzer extends AbstractImportAnalyzer {

    public ControllerImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Controller class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                Imports.JAVA_GENERATED,
                Imports.SPRING_REST_CONTROLLER,
                Imports.SPRING_REQUEST_MAPPING,
                Imports.SPRING_GET_MAPPING,
                Imports.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR,
                Imports.JAVA_LIST,
                Imports.SPRING_RESPONSE_ENTITY
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.DTO, userConfig, entity.getClassName()));
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.SERVICE, userConfig, entity.getClassName()));
    }
}
