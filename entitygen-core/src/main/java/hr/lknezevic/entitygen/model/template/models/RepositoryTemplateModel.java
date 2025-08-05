package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.Getter;

import java.util.List;

/**
 * Represents the model for a repository template.
 * This class extends AbstractTemplateModel and provides the body of the repository template.
 * It includes methods to retrieve the component name, entity name, and ID type.
 *
 * @author leonknezevic
 */
@Getter
public class RepositoryTemplateModel extends AbstractTemplateModel {

    public RepositoryTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateFactory.builder()
                .template(TemplateConst.REPOSITORY_IMPLEMENTATION)
                .build()
                .addParams(getComponentName(), getEntityName(), getIdType())
                .format();
    }

    private String getIdType() {
        return TemplateUtil.getEntityIdType(entity);
    }

}
