package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;

public class DtoImportAnalyzer extends AbstractImportAnalyzer {

    public DtoImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        if (hasAdditionalImports())
            analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        if (entity.isCompositeKey())
            otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, entity.getEmbeddedId().getClassName()));

        entity.getRelations().forEach(relation -> {
            Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());
            if (targetEntity != null && targetEntity.isCompositeKey()) {
                otherImports.add(TemplateUtil.getComponentImport(ComponentType.EMBEDDABLE, userConfig, targetEntity.getEmbeddedId().getClassName()));
            }
        });

        analyzeFields();
        analyzeRelations();
    }

    @Override
    protected boolean hasAdditionalImports() {
        return true;
    }
}
