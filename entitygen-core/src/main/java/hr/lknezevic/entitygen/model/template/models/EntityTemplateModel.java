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
public class EntityTemplateModel extends AbstractTemplateModel {

    public EntityTemplateModel(TemplateProviderObject tpo, List<String> imports) {
        super(tpo.componentType(), tpo.entity(), tpo.userConfig(), tpo.entityByClassName(), imports);
    }

    @Override
    public String getModelBody() {
        return TemplateUtil.joinParams(TemplateConst.NEW_LINE,
                getPrimaryKey(), getRegularFields(), getAllRelations());
    }

    private String getPrimaryKey() {
        return PrimaryKeyModule.builder()
                .componentType(ComponentType.ENTITY)
                .entity(entity)
                .build()
                .construct();
    }

    private String getRegularFields() {
        List<String> fields = entity.getFields().stream()
                .filter(field -> !field.isPrimaryKey())
                .map(field ->
                        FieldModule.builder()
                                .componentType(ComponentType.ENTITY)
                                .field(field)
                                .build()
                                .construct())
                .toList();

        return String.join(TemplateConst.NEW_LINE, fields);
    }

    private String getAllRelations() {
        List<String> relations = new ArrayList<>();

        if (entity.getRelations().isEmpty()) return "";

        for (Relation relation : entity.getRelations()) {
            Entity targetEntity = entityByClassName.get(relation.getTargetEntityClass());

            String relationField = RelationModule.builder()
                    .componentType(ComponentType.ENTITY)
                    .relation(relation)
                    .sourceEntity(entity)
                    .targetEntity(targetEntity)
                    .build()
                    .construct();

            relations.add(relationField);
        }

        return String.join(TemplateConst.NEW_LINE + TemplateConst.NEW_LINE, relations);
    }

}
