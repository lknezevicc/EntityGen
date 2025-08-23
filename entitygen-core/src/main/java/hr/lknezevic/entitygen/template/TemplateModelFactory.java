package hr.lknezevic.entitygen.template;

import hr.lknezevic.entitygen.model.template.models.*;
import hr.lknezevic.entitygen.template.models.*;

import java.util.List;

/**
 * Factory class for creating instances of template models based on the provided TemplateProviderObject.
 */
public class TemplateModelFactory {

    /**
     * Creates an instance of TemplateModel based on the provided TemplateProviderObject and imports.
     *
     * @param tpo the TemplateProviderObject
     * @param imports a list of import statements to be included in the template model
     * @return an instance of TemplateModel
     */
    public static TemplateModel createModel(TemplateProviderObject tpo, List<String> imports) {
        return switch (tpo.componentType()) {
            case ENTITY -> new EntityTemplateModel(tpo, imports);
            case DTO -> new DtoTemplateModel(tpo, imports);
            case REPOSITORY -> new RepositoryTemplateModel(tpo, imports);
            case SERVICE -> new ServiceTemplateModel(tpo, imports);
            case SERVICE_IMPL -> new ServiceImplTemplateModel(tpo, imports);
            case CONTROLLER -> new ControllerTemplateModel(tpo, imports);
            case EMBEDDABLE -> new EmbeddableTemplateModel(tpo, imports);
        };
    }

}
