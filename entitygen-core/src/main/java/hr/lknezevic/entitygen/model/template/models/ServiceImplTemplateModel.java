package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.model.template.modules.FieldModule;
import hr.lknezevic.entitygen.model.template.modules.PrimaryKeyModule;
import hr.lknezevic.entitygen.model.template.modules.RelationModule;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class ServiceImplTemplateModel extends AbstractTemplateModel {

    public ServiceImplTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE,
                getPrimaryKeyConstructorParam(), getFieldsConstructorParams(), getRelationConstructorParams());
    }

    private String getPrimaryKeyConstructorParam() {
        return PrimaryKeyModule.builder()
                .componentType(ComponentType.SERVICE_IMPL)
                .entity(entity)
                .build()
                .construct();
    }

    private String getFieldsConstructorParams() {
        List<String> params = entity.getFields().stream()
                .filter(field -> !field.isPrimaryKey())
                .map(field ->
                        FieldModule.builder()
                                .componentType(ComponentType.SERVICE_IMPL)
                                .field(field)
                                .build()
                                .construct())
                .toList();

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, params);
    }

    private String getRelationConstructorParams() {
        List<String> params = new ArrayList<>();

        if (entity.getRelations().isEmpty()) return "";

        for (Relation relation : entity.getRelations()) {
            Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());

            String param = RelationModule.builder()
                    .componentType(ComponentType.SERVICE_IMPL)
                    .sourceEntity(entity)
                    .relation(relation)
                    .targetEntity(targetEntity)
                    .build()
                    .construct();

            params.add(param);
        }

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, params);
    }

}
