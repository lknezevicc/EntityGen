package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.util.Map;

public record TemplateProviderObject(
        ComponentType componentType,
        Entity entity,
        UserConfig userConfig,
        Map<String, Entity> entityByClassName
) { }
