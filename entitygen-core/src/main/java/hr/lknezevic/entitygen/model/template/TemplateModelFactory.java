package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.models.*;

import java.util.List;
import java.util.Map;

public class TemplateModelFactory {
    public static TemplateModel createModel(ComponentType componentType, Entity entity, UserConfig config,
                                            List<String> imports, Map<String, Entity> entityByClassName) {

        return switch (componentType) {
            case ENTITY -> new EntityTemplateModel(componentType, entity, config, imports, entityByClassName);
            case DTO -> new DtoTemplateModel(componentType, entity, config, imports, entityByClassName);
            case REPOSITORY -> new RepositoryTemplateModel(componentType, entity, config, imports, entityByClassName);
            case SERVICE -> new ServiceTemplateModel(componentType, entity, config, imports, entityByClassName);
            case SERVICE_IMPL -> new ServiceImplTemplateModel(componentType, entity, config, imports, entityByClassName);
            case CONTROLLER -> new ControllerTemplateModel(componentType, entity, config, imports, entityByClassName);
            case EMBEDDABLE -> new EmbeddableTemplateModel(componentType, entity, config, imports, entityByClassName);
        };
    }
}
