package hr.lknezevic.entitygen.helper;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hr.lknezevic.entitygen.analyzer.ImportFactory;
import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.exceptions.unchecked.TemplateGenerationException;
import hr.lknezevic.entitygen.template.TemplateModelFactory;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.domain.Entity;
import hr.lknezevic.entitygen.template.models.TemplateModel;
import hr.lknezevic.entitygen.utils.LoggingUtility;
import hr.lknezevic.entitygen.utils.TemplateUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for FreeMarker template processing and component generation.
 * <p>
 * Handles template configuration, file path resolution, and component generation logic
 * based on {@link UserConfig} settings.
 */
public class TemplateRunnerHelper {
    private final static String JAVA_EXTENSION = ".java";
    private final UserConfig userConfig;
    private final Configuration cfg;
    private final JavaCodeFormatter formatter;
    private Map<String, Entity> entityByClassName;

    public TemplateRunnerHelper(UserConfig userConfig) {
        this.userConfig = userConfig;
        this.formatter = new JavaCodeFormatter();
        this.entityByClassName = new HashMap<>();

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
     */
    public void generateComponents(Entity entity) {
        List<ComponentType> generatedComponents = new ArrayList<>();
        for (ComponentType componentType : ComponentType.values()) {

            TemplateProviderObject tpo = new TemplateProviderObject(componentType, entity, userConfig, entityByClassName);

            if (componentType == ComponentType.EMBEDDABLE && !entity.isCompositeKey()) {
                LoggingUtility.debug("Skipping EMBEDDABLE generation for {} - no composite key",
                        entity.getClassName());
                continue;
            }

            if (!TemplateUtil.shouldGenerate(componentType, userConfig)) {
                LoggingUtility.debug("Skipping {} generation for {}",
                        componentType.name(), entity.getClassName());
                continue;
            }

            List<String> imports = ImportFactory.getAnalyzer(tpo).getImports();
            TemplateModel template = TemplateModelFactory.createModel(tpo, imports);

            String filePath = template.getComponentPackagePath() + File.separator +
                    template.getComponentName() + JAVA_EXTENSION;

            File file = new File(filePath);

            if (!TemplateUtil.overwriteComponent(file, userConfig)) {
                LoggingUtility.debug("Skipping {} generation for {} - file already exists",
                        componentType.name(), entity.getClassName());
                continue;
            }

            if (file.getParentFile().mkdirs())
                LoggingUtility.debug("Created parent directory for {}", file.getParentFile().getAbsolutePath());

            Map<String, Object> data = new HashMap<>();
            data.put("date", TemplateUtil.getCurrentDate());
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

                generatedComponents.add(componentType);
            } catch (TemplateException | IOException e) {
                LoggingUtility.error("Failed to generate {} for {}: {}", componentType.name(), entity.getClassName(), e.getMessage());
                throw new TemplateGenerationException(String.format("Error generating %s for %s", componentType.name(), entity.getClassName()), e);
            }
        }

        LoggingUtility.info("Generated components for {}: {}", entity.getClassName(), generatedComponents);
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

    public void setEntityByClassName(List<Entity> entities) {
        this.entityByClassName = entities.stream()
                .collect(java.util.stream.Collectors.toMap(
                        Entity::getClassName,
                        e -> e,
                        (existing, replacement) -> existing
                ));
    }
}
