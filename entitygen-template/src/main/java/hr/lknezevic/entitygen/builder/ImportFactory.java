package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.builder.imports.*;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;

public class ImportFactory {

    public static ImportAnalyzer getAnalyzer(ComponentType componentType, TemplateProviderObject tpo) {
        return switch (componentType) {
            case EMBEDDABLE -> new EmbeddableImportAnalyzer(tpo);
            case ENTITY ->  new EntityImportAnalyzer(tpo);
            case DTO -> new DtoImportAnalyzer(tpo);
            case REPOSITORY -> new RepositoryImportAnalyzer(tpo);
            case SERVICE -> new ServiceImportAnalyzer(tpo);
            case SERVICE_IMPL -> new ServiceImplImportAnalyzer(tpo);
            case CONTROLLER -> new ControllerImportAnalyzer(tpo);
        };
    }

}
