package hr.lknezevic.entitygen.builder.relation;

import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.List;

public interface RelationAnalyzer {
    List<Relation> buildSpecificRelations();
}
