package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.Getter;

import java.util.List;

/**
 * Represents a Data Transfer Object (DTO) template model.
 * This model is used to generate the body of a DTO class based on the provided template provider object.
 *
 * @author leonknezevic
 */
@Getter
public class DtoTemplateModel extends AbstractTemplateModel {

    public DtoTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE,
                getPrimaryKey(), getNonPrimaryFields(), getRelationFields());
    }

}
