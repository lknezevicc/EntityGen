package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

public class ControllerImportAnalyzer extends AbstractImportAnalyzer {

    public ControllerImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.SPRING_REST_CONTROLLER,
                TemplateImport.SPRING_REQUEST_MAPPING,
                TemplateImport.SPRING_GET_MAPPING,
                TemplateImport.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR,
                TemplateImport.JAVA_LIST,
                TemplateImport.SPRING_RESPONSE_ENTITY
        ));

        if (hasAdditionalImports())
            analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.DTO, userConfig, entity.getClassName()));
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.SERVICE, userConfig, entity.getClassName()));
    }

    @Override
    protected boolean hasAdditionalImports() {
        return true;
    }
}
