package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.modules.FieldModule;
import lombok.Getter;

import java.util.List;


@Getter
public class EmbeddableTemplateModel extends AbstractTemplateModel {

    public EmbeddableTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        List<String> fields = entity.getEmbeddedId()
                .getFields()
                .stream()
                .map(field ->
                        FieldModule.builder()
                                .componentType(ComponentType.EMBEDDABLE)
                                .field(field)
                                .build()
                                .construct()
                ).toList();

        return String.join(TemplateConst.NEW_LINE, fields);
    }

}
