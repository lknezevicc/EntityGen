package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.common.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ControllerTemplateModel extends TemplateModel {

    public ControllerTemplateModel(ComponentType componentType, Entity entity, UserConfig config, List<String> imports, Map<String, Entity> entityByClassName) {
        super(componentType, entity, config, imports, entityByClassName);
    }

    public String getControllerName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.CONTROLLER);
    }

    public String getServiceName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.SERVICE);
    }

    public String getDtoName() {
        return entity.getClassName() + getComponentSuffix(ComponentType.DTO);
    }

    @Override
    public List<String> getAllImports() {
        List<String> allImports = new ArrayList<>(imports);
        allImports.addAll(addAdditionalImports());

        return allImports;
    }

    private List<String> addAdditionalImports() {
        List<String> imports = new ArrayList<>();
        imports.add(config.getServicePackage() + "." + getServiceName());
        imports.add(config.getDtoPackage() + "." + getDtoName());
        imports.sort(String::compareTo);

        return imports;
    }
}
