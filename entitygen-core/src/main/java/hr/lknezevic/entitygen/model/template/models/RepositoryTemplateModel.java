package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepositoryTemplateModel extends TemplateModel {

    public RepositoryTemplateModel(ComponentType componentType, Entity entity, UserConfig config, List<String> imports, Map<String, Entity> entityByClassName) {
        super(componentType, entity, config, imports, entityByClassName);
    }

    public String getEntityName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.ENTITY);
    }

    @Override
    public List<String> getAllImports() {
        List<String> allImports = new ArrayList<>(imports);
        allImports.add(config.getEntityPackage() + "." + getEntityName());
        if (entity.isCompositeKey() && entity.getEmbeddedId() != null)
            allImports.add(config.getEntityPackage() + "." + entity.getEmbeddedId().getClassName());

        return allImports;
    }

    public String getIdType() {
        String primaryKey;
        if (entity.isCompositeKey() && entity.getEmbeddedId() != null)
            primaryKey = entity.getEmbeddedId().getClassName();
        else
            primaryKey = entity.getPrimaryKeyFields().getFirst().getJavaType();

        return primaryKey;
    }

}
