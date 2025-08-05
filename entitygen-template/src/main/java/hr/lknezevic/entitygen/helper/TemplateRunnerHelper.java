package hr.lknezevic.entitygen.helper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hr.lknezevic.entitygen.builder.ImportFactory;
import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateModelFactory;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.models.TemplateModel;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for FreeMarker template processing and component generation.
 * <p>
 * Handles template configuration, file path resolution, and component generation logic
 * based on {@link UserConfig} settings.
 *
 * @author leonknezevic
 */
@Slf4j
public class TemplateRunnerHelper {
    private final static String JAVA_EXTENSION = ".java";
    private final UserConfig userConfig;
    private final Configuration cfg;
    private final JavaCodeFormatter formatter = new JavaCodeFormatter();

    public TemplateRunnerHelper(UserConfig userConfig) {
        this.userConfig = userConfig;

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setWhitespaceStripping(true);

        this.cfg = cfg;
    }

    /**
     * Generates component types for the given entity.
     *
     * @param entity the entity to generate component for
     * @param entityByClassName map of table name to entity for relation lookups
     */
    public void generateComponents(Entity entity, Map<String, Entity> entityByClassName) {
        for (ComponentType componentType : ComponentType.values()) {

            TemplateProviderObject tpo = new TemplateProviderObject(componentType, entity, userConfig, entityByClassName);

            if (componentType == ComponentType.EMBEDDABLE && !entity.isCompositeKey()) {
                log.debug("Skipping EMBEDDABLE generation for {} - no composite key", entity.getClassName());
                continue;
            }

            if (!TemplateUtil.shouldGenerate(componentType, userConfig)) {
                log.info("Skipping {} generation for {}", componentType, entity.getClassName());
                continue;
            }

            List<String> imports = ImportFactory.getAnalyzer(componentType, tpo).getImports();
            TemplateModel template = TemplateModelFactory.createModel(tpo, imports);

            String filePath = template.getComponentPackagePath() + File.separator +
                    template.getComponentName() + JAVA_EXTENSION;

            File file = new File(filePath);

            if (!TemplateUtil.overwriteComponent(componentType, file, userConfig)) {
                log.info("Skipping {} generation for {} - file already exists", componentType, entity.getClassName());
                continue;
            }

            if (file.getParentFile().mkdirs())
                log.debug("Created {} parent directory", file.getParentFile().getAbsolutePath());

            Map<String, Object> data = new HashMap<>();
            data.put("template", template);

            try (StringWriter stringWriter = new StringWriter()) {
                getTemplate(componentType).process(data, stringWriter);

                String generatedCode = stringWriter.toString();
                String formattedCode = formatter.formatJavaCode(generatedCode);

                if (componentType == ComponentType.ENTITY || componentType == ComponentType.EMBEDDABLE) {
                    formattedCode = formatter.formatComponent(formattedCode);
                }

                try (FileWriter fileWriter = new FileWriter(file)) {
                    fileWriter.write(formattedCode);
                }

                log.info("Generated {} for {}", componentType, entity.getClassName());
            } catch (TemplateException | IOException e) {
                log.error("Failed to generate {} for {}: {}", componentType, entity.getClassName(), e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private Template getTemplate(ComponentType componentType) throws IOException {
        return switch (componentType) {
            case ENTITY -> cfg.getTemplate("entity.ftl");
            case EMBEDDABLE -> cfg.getTemplate("embeddable.ftl");
            case DTO -> cfg.getTemplate("dto.ftl");
            case SERVICE -> cfg.getTemplate("service.ftl");
            case SERVICE_IMPL -> cfg.getTemplate("service-impl.ftl");
            case CONTROLLER -> cfg.getTemplate("controller.ftl");
            case REPOSITORY -> cfg.getTemplate("repository.ftl");
        };
    }
}
