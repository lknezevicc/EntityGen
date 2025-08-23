package hr.lknezevic.entitygen.template.models;

import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import lombok.Getter;

import java.util.List;

/**
 * Represents a template model for a service in the EntityGen framework.
 * This class extends AbstractTemplateModel and provides the body of the service
 * using a predefined template.
 */
@Getter
public class ServiceTemplateModel extends AbstractTemplateModel {

    public ServiceTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    /**
     * Returns the body for the service Freemarker template.
     *
     * @return formatted string representing the service model body
     */
    @Override
    public String getModelBody() {
        return TemplateFactory.builder()
                .template(TemplateConst.SERVICE_FIND_ALL_METHOD)
                .build()
                .addParam(getDtoName())
                .format();
    }

}
