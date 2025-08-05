package hr.lknezevic.entitygen.model.template;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.util.Map;

/**
 * Represents an object that provides context for template generation.
 * It includes the component type, entity, user configuration, and a map of entities by class name.
 *
 * @author leonknezevic
 */
public record TemplateProviderObject(
        ComponentType componentType,
        Entity entity,
        UserConfig userConfig,
        Map<String, Entity> entityByClassName
) { }
