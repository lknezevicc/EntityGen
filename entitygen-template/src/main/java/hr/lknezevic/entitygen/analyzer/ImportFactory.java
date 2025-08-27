package hr.lknezevic.entitygen.analyzer;

import hr.lknezevic.entitygen.analyzer.imports.*;
import hr.lknezevic.entitygen.template.TemplateProviderObject;

/**
 * Factory class for creating instances of ImportAnalyzer based on the component type.
 */
public class ImportFactory {

    private ImportFactory() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns an ImportAnalyzer instance based on the provided component type
     *
     * @param tpo the TemplateProviderObject containing necessary information
     * @return an instance of ImportAnalyzer
     */
    public static ImportAnalyzer getAnalyzer(TemplateProviderObject tpo) {
        return switch (tpo.componentType()) {
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
