package hr.lknezevic.entitygen.analyzer.relation;

import hr.lknezevic.entitygen.model.domain.Relation;

import java.util.List;

/**
 * Interface for analyzing and building specific relations between entities.
 */
public interface RelationAnalyzer {
    List<Relation> buildSpecificRelations();
}
