package hr.lknezevic.entitygen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import hr.lknezevic.entitygen.mapper.DefaultEntityModelMapper;
import hr.lknezevic.entitygen.mapper.EntityModelMapper;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityTemplateRunner {
    private final static String JAVA_EXTENSION = ".java";
    private final Configuration cfg;

    public EntityTemplateRunner() {
        cfg = new Configuration(Configuration.VERSION_2_3_32);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public void generateEntity(Table table, String packageName, File outputDir) throws IOException {
        Template entityTemplate = cfg.getTemplate("entity.ftl");
        Template embeddedTemplate = cfg.getTemplate("embeddable.ftl");

        EntityModelMapper entityModelMapper = new DefaultEntityModelMapper();
        List<Entity> entities = entityModelMapper.mapEntities(List.of(table));

        for (Entity entity : entities) {
            Map<String, Object> data = new HashMap<>();
            data.put("noLength", ExcludeConsts.TYPES_WITHOUT_LENGTH);
            data.put("packageName", packageName);
            data.put("entity", entity);

            if (entity.isHasCompositeKey()) {
                File outEmbeddedFile = new File(outputDir, entity.getEmbeddedId().getClassName() + JAVA_EXTENSION);
                try (Writer out = new FileWriter(outEmbeddedFile)) {
                    embeddedTemplate.process(data, out);
                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }
            }

            File outEntityFile = new File(outputDir, entity.getClassName() + JAVA_EXTENSION);
            try (Writer out = new FileWriter(outEntityFile)) {
                entityTemplate.process(data, out);
            } catch (TemplateException e) {
                throw new RuntimeException(e);
            }
        }
    }
}