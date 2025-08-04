package hr.lknezevic.entitygen.builder.imports;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.enums.TemplateImport;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class ServiceImplImportAnalyzer extends AbstractImportAnalyzer {

    public ServiceImplImportAnalyzer(TemplateProviderObject tpo) {
        super(tpo.entity(), tpo.userConfig(), tpo.entityByClassName());
    }

    @Override
    public List<String> getImports() {
        imports.addAll(Set.of(
                TemplateImport.SPRING_SERVICE,
                TemplateImport.LOMBOK_REQUIRED_ARGS_CONSTRUCTOR,
                TemplateImport.JAVA_LIST
        ));

        if (hasAdditionalImports())
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
    protected boolean hasAdditionalImports() {
        return true;
    }

    @Override
    protected void analyzeRelations() {
        if (entity.getRelations().isEmpty()) return;

        if (entity.getRelations().stream().anyMatch(relation -> {
            RelationType relationType = relation.getType();
            return relationType == RelationType.ONE_TO_MANY || relationType == RelationType.MANY_TO_MANY;
        })) {
            imports.add(TemplateImport.JAVA_COLLECTORS);
        }

        entity.getRelations().forEach(relation -> {
            CollectionType collectionType = relation.getCollectionType();
            if (collectionType == null) return;
            switch (collectionType) {
                case LIST -> {
                    imports.add(TemplateImport.JAVA_LIST);
                    imports.add(TemplateImport.JAVA_ARRAY_LIST);
                }
                case SET -> {
                    imports.add(TemplateImport.JAVA_SET);
                    imports.add(TemplateImport.JAVA_HASH_SET);
                }
                case LINKED_HASH_SET -> {
                    imports.add(TemplateImport.JAVA_SET);
                    imports.add(TemplateImport.JAVA_LINKED_HASH_SET);
                }
            }
        });
    }
}
