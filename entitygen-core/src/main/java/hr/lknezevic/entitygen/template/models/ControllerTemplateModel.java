package hr.lknezevic.entitygen.template.models;

import hr.lknezevic.entitygen.template.TemplateConst;
import hr.lknezevic.entitygen.template.TemplateFactory;
import hr.lknezevic.entitygen.template.TemplateProviderObject;
import lombok.Getter;

import java.util.List;

/**
 * Represents a template model for a controller in the EntityGen framework.
 * This class extends AbstractTemplateModel and provides the body of the controller
 * using a predefined template.
 */
@Getter
public class ControllerTemplateModel extends AbstractTemplateModel {

    public ControllerTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    /**
     * Returns the body for the controller Freemarker template.
     *
     * @return formatted string representing the controller model body
     */
    @Override
    public String getModelBody() {
        return TemplateFactory.builder()
                .template(TemplateConst.CONTROLLER_GET_ALL)
                .build()
                .addParams(getDtoName(), getDtoName())
                .format();
    }

}
