package hr.lknezevic.entitygen.model.template.models;

import hr.lknezevic.entitygen.enums.ComponentType;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateProviderObject;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.modules.FieldModule;
import hr.lknezevic.entitygen.model.template.modules.PrimaryKeyModule;
import hr.lknezevic.entitygen.model.template.modules.RelationModule;
import hr.lknezevic.entitygen.utils.TemplateUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DtoTemplateModel extends AbstractTemplateModel {

    public DtoTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE,
                getIdField(), getNonPrimaryFields(), getRelationFields());
    }

    private String getIdField() {
        return PrimaryKeyModule.builder()
                .componentType(ComponentType.DTO)
                .entity(entity)
                .build()
                .construct();
    }

    private String getNonPrimaryFields() {
        List<String> fields = entity.getFields().stream()
                .filter(field -> !field.isPrimaryKey())
                .map(field -> FieldModule.builder()
                        .componentType(ComponentType.DTO)
                        .field(field)
                        .build()
                        .construct()
                )
                .toList();

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, fields);
    }

    private String getRelationFields() {
        List<String> relationFields = new ArrayList<>();

        entity.getRelations().forEach(relation -> {
            Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());
            if (targetEntity == null) return;
            
            String relationField = RelationModule.builder()
                    .componentType(ComponentType.DTO)
                    .sourceEntity(entity)
                    .relation(relation)
                    .targetEntity(targetEntity)
                    .build()
                    .construct();

            relationFields.add(relationField);
        });

        return String.join(TemplateConst.COMMA_JOIN_NEWLINE, relationFields);
    }

}
