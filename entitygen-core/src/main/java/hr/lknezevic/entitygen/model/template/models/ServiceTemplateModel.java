package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import lombok.Getter;

import java.util.List;

/**
 * Represents a template model for a service in the EntityGen framework.
 * This class extends AbstractTemplateModel and provides the body of the service
 * using a predefined template.
 *
 * @author leonknezevic
 */
@Getter
public class ServiceTemplateModel extends AbstractTemplateModel {

    public ServiceTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateFactory.builder()
                .template(TemplateConst.SERVICE_FIND_ALL_METHOD)
                .build()
                .addParam(getDtoName())
                .format();
    }

}
