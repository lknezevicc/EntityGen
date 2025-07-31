package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmbeddableTemplateModel extends TemplateModel {

    public EmbeddableTemplateModel(ComponentType componentType, Entity entity, UserConfig config, List<String> imports, Map<String, Entity> entityByClassName) {
        super(componentType, entity, config, imports, entityByClassName);
    }

    @Override
    public List<String> getAllImports() {
        return new ArrayList<>(imports);
    }

    public String getColumnParams(Field field) {
        StringBuilder params = new StringBuilder();

        params.append("name = \"").append(field.getColumnName()).append("\"");

        if (!field.isNullable()) {
            params.append(", nullable = false");
        }

        if (field.getLength() != null && isStringType(field.getJavaType())) {
            params.append(", length = ").append(field.getLength());
        }

//        if (field.getPrecision() != null) {
//            params.append(", precision = ").append(field.getPrecision());
//        }
//        if (field.getScale() != null) {
//            params.append(", scale = ").append(field.getScale());
//        }
        
        return params.toString();
    }

    private boolean isStringType(String javaType) {
        return "String".equals(javaType);
    }
}
