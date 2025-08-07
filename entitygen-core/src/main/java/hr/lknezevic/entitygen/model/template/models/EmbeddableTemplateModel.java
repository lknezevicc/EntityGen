package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.modules.FieldModule;
import lombok.Getter;

import java.util.List;

/**
 * Represents a template model for an embeddable entity.
 * It constructs the model body by generating fields based on the embedded ID of the entity.
 */
@Getter
public class EmbeddableTemplateModel extends AbstractTemplateModel {

    public EmbeddableTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    /**
     * Returns the body for the embeddable Freemarker template.
     *
     * @return formatted string representing the embeddable model body
     */
    @Override
    public String getModelBody() {
        List<String> fields = entity.getEmbeddedId().getFields()
                .stream()
                .map(field ->
                        FieldModule.builder()
                                .componentType(componentType)
                                .field(field)
                                .build()
                                .construct()
                ).toList();

        return String.join(TemplateConst.NEW_LINE, fields);
    }

}
