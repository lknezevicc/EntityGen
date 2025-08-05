package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import lombok.Getter;

import java.util.List;

/**
 * Represents a template model for a controller in the EntityGen framework.
 * This class extends AbstractTemplateModel and provides the body of the controller
 * using a predefined template.
 *
 * @author leonknezevic
 */
@Getter
public class ControllerTemplateModel extends AbstractTemplateModel {

    public ControllerTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateFactory.builder()
                .template(TemplateConst.CONTROLLER_GET_ALL)
                .build()
                .addParams(getDtoName(), getDtoName())
                .format();
    }

}
