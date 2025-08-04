package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.model.template.models.*;

import java.util.List;

public class TemplateModelFactory {

    public static AbstractTemplateModel createModel(TemplateProviderObject tpo, List<String> imports) {

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
