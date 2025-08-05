package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.Getter;

import java.util.List;

/**
 * Represents the model for a service implementation template.
 * This class extends AbstractTemplateModel and provides the body of the service implementation template.
 * It includes methods to retrieve primary keys, non-primary fields, and relation fields.
 *
 * @author leonknezevic
 */
@Getter
public class ServiceImplTemplateModel extends AbstractTemplateModel {

    public ServiceImplTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE,
                getPrimaryKey(), getNonPrimaryFields(), getRelationFields());
    }

}
