package hr.lknezevic.entitygen.model.template.models;

import java.util.List;

public interface TemplateModel {
    String getComponentName();
    String getComponentPackage();
    String getEmbeddedIdName();
    String getEntityName();
    String getDtoName();
    String getRepositoryName();
    String getServiceName();
    String getServiceImplName();
    String getControllerName();
    String getComponentPackagePath();
    List<String> getImports();
    String getModelBody();
}
