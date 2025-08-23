package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;

/**
 * Analyzes and provides the necessary imports for a Data Transfer Object (DTO) class.
 */
public class DtoImportAnalyzer extends AbstractImportAnalyzer {

    public DtoImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the DTO class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
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
}
