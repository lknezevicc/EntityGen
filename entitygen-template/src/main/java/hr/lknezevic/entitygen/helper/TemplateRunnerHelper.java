package hr.lknezevic.entitygen.helper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for FreeMarker template processing and component generation.
 * <p>
 * Handles template configuration, file path resolution, and component generation logic
 * based on {@link UserConfig} settings.
 *
 * @author Leon Knežević
 */
@Slf4j
public class TemplateRunnerHelper {
    private final static String JAVA_EXTENSION = ".java";
    private final UserConfig userConfig;
    private final Configuration cfg;
    private final ImportAnalyzer importAnalyzer;

    public TemplateRunnerHelper(UserConfig userConfig) {
        importAnalyzer = new ImportAnalyzer();
        this.userConfig = userConfig;

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setWhitespaceStripping(true);

        this.cfg = cfg;
    }

    /**
     * Generates a specific component type for the given entity.
     * <p>
     * Skips generation if component is disabled in configuration or not applicable
     *
     * @param entity the entity to generate component for
     * @param entityByTableName map of table name to entity for relation lookups
     */
    public void generateComponents(Entity entity, Map<String, Entity> entityByClassName) {
        for (ComponentType componentType : ComponentType.values()) {
            if (componentType == ComponentType.EMBEDDABLE && !entity.isCompositeKey()) {
                log.debug("Skipping EMBEDDABLE generation for {} - no composite key", entity.getClassName());
                continue;
            }

            if (!shouldGenerate(componentType)) {
                log.info("Skipping {} generation for {}", componentType, entity.getClassName());
                continue;
            }

            // TODO IMPORT ANALYZER
            List<String> imports = importAnalyzer.analyzeFor(entity, componentType).getSortedImports();

            String fileName;
            if (componentType == ComponentType.EMBEDDABLE && entity.isCompositeKey()) {
                fileName = getComponentPackagePath(componentType) + File.separator +
                        entity.getEmbeddedId().getClassName() + getComponentSuffix(componentType) + JAVA_EXTENSION;
            } else {
                fileName = getComponentPackagePath(componentType) + File.separator +
                        entity.getClassName() + getComponentSuffix(componentType) + JAVA_EXTENSION;
            }

            File file = new File(fileName);
            if (file.getParentFile().mkdirs()) log.debug("Created {} parent directory", file.getParentFile().getAbsolutePath());

            Map<String, Object> data = new HashMap<>();
            data.put("cfg", userConfig);
            data.put("entity", entity);
            data.put("entityByClassName", entityByClassName);

            try (Writer out = new FileWriter(file)) {
                getTemplate(componentType).process(data, out);
                log.info("Generated {} for {}", componentType, entity.getClassName());
            } catch (TemplateException | IOException e) {
                log.error("Failed to generate {} for {}: {}", componentType, entity.getClassName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private Template getTemplate(ComponentType componentType) throws IOException {
        return switch (componentType) {
            case EMBEDDABLE -> cfg.getTemplate("embeddable.ftl");
            case DTO -> cfg.getTemplate("dto.ftl");
            case SERVICE -> cfg.getTemplate("service.ftl");
            case SERVICE_IMPL -> cfg.getTemplate("service-impl.ftl");
            case CONTROLLER -> cfg.getTemplate("controller.ftl");
            case REPOSITORY -> cfg.getTemplate("repository.ftl");
            default -> cfg.getTemplate("entity.ftl");
        };
    }

    private Boolean shouldGenerate(ComponentType componentType) {
        return switch (componentType) {
            case DTO -> userConfig.getGenerateDtos();
            case SERVICE, SERVICE_IMPL -> userConfig.getGenerateServices();
            case CONTROLLER -> userConfig.getGenerateControllers();
            case REPOSITORY -> userConfig.getGenerateRepositories();
            default -> true;
        };
    }
}
