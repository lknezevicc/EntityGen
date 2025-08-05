package hr.lknezevic.entitygen.model.template.models;

import java.util.List;

/**
 * Interface representing a template model for various components in the EntityGen framework.
 * This interface defines methods to retrieve component names, package information, and model body.
 * It is used to generate templates for entities, DTOs, repositories, services, and controllers.
 *
 * @author leonknezevic
 */
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
