package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

public class RepositoryImportAnalyzer extends AbstractImportAnalyzer {

    public RepositoryImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.SPRING_REPOSITORY,
                TemplateImport.SPRING_JPA_REPOSITORY
        ));

        if (hasAdditionalImports())
            analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.ENTITY, userConfig, entity.getClassName()));
        if (entity.isCompositeKey())
            otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, entity.getEmbeddedId().getClassName()));
    }

    @Override
    protected boolean hasAdditionalImports() {
        return true;
    }
}
