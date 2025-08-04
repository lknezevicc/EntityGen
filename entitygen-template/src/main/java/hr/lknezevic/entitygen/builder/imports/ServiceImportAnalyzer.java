package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;

public class ServiceImportAnalyzer extends AbstractImportAnalyzer {

    public ServiceImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        imports.add(TemplateImport.JAVA_LIST);

        if (hasAdditionalImports())
            analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.DTO, userConfig, entity.getClassName()));
    }

    @Override
    protected boolean hasAdditionalImports() {
        return true;
    }
}
