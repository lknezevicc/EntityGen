package hr.lknezevic.entitygen.analyzer.imports;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.util.List;
import java.util.Set;

/**
 * Analyzes and provides the necessary imports for a Service Implementation class.
 */
public class ServiceImplImportAnalyzer extends AbstractImportAnalyzer {

    public ServiceImplImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    /**
     * Returns a list of imports required for the Service Implementation class.
     *
     * @return List of import strings
     */
    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.SPRING_SERVICE,
                TemplateImport.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR,
                TemplateImport.JAVA_LIST
        ));

        analyzeAdditionalImports();

        return getCombinedImports();
    }

    @Override
    protected void analyzeAdditionalImports() {
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.SERVICE, userConfig, entity.getClassName()));
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.REPOSITORY, userConfig, entity.getClassName()));
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.ENTITY, userConfig, entity.getClassName()));
        otherImports.add(TemplateUtil.getComponentImport(ComponentType.DTO, userConfig, entity.getClassName()));

        this.analyzeRelations();
    }

    @Override
    protected void analyzeRelations() {
        if (entity.getRelations().isEmpty()) return;

        if (entity.getRelations().stream().anyMatch(relation -> {
            RelationType relationType = relation.getType();
            return relationType == RelationType.ONE_TO_MANY || relationType == RelationType.MANY_TO_MANY;
        })) {
            imports.add(TemplateImport.JAVA_COLLECTORS);
            imports.add(TemplateImport.JAVA_ARRAY_LIST);
        }
    }
}
