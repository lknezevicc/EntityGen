package hr.lknezevic.entitygen.analyzer.relation;

import hr.lknezevic.entitygen.analyzer.RelationBuilder;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.utils.LoggingUtility;
import hr.lknezevic.entitygen.utils.NamingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * InverseRelationBuilder is responsible for building inverse relations
 * (N:1 and 1:1) based on foreign keys that reference the current table.
 * It detects self-referencing relations and handles them appropriately.
 */
public class InverseRelationAnalyzer extends AbstractRelationAnalyzer {

    public InverseRelationAnalyzer(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    /**
     * Builds specific inverse relations for the current table.
     * It identifies tables that reference the current table and creates inverse relations
     * based on the foreign keys defined in those tables.
     *
     * @return a list of inverse relations for the current table
     */
    @Override
    public List<Relation> buildSpecificRelations() {
        List<Relation> relations = new ArrayList<>();

        // Find tables that reference the current table
        for (Table candidateTable : getContext().getAllTables()) {
            List<ForeignKey> referencingFKs = candidateTable.getForeignKeys().stream()
                    .filter(fk -> fk.getReferencedTable().equals(table.getName()))
                    .toList();

            if (referencingFKs.isEmpty()) continue;

            // Check if the candidate table is self-referencing
            boolean isSelfReferencing = candidateTable.getName().equals(table.getName());

            // Group FKs by constraint name
            Map<String, List<ForeignKey>> groupedFKs = RelationDetector.groupForeignKeysByConstraint(referencingFKs);

            for (var entry : groupedFKs.entrySet()) {
                List<ForeignKey> fkGroup = entry.getValue();

                Relation inverseRelation = buildInverseRelation(candidateTable, fkGroup, isSelfReferencing);

                String relationKey = RelationDetector.generateRelationKey(
                        table.getName(),
                        candidateTable.getName(),
                        inverseRelation.getType().name() + "_INVERSE"
                );

                if (relationNotProcessed(relationKey)) {
                    relations.add(inverseRelation);
                    addProcessedRelation(relationKey);
                }
            }
        }

        return relations;
    }

    private Relation buildInverseRelation(Table candidateTable, List<ForeignKey> fkGroup, boolean isSelfReferencing) {
        // Check if the relation is one-to-one or one-to-many
        boolean isOneToOne = RelationDetector.isOneToOneRelation(candidateTable, fkGroup);
        RelationType relationType = isOneToOne ? RelationType.ONE_TO_ONE : RelationType.ONE_TO_MANY;
        String mappedByField = isSelfReferencing ?
                "parent" + NamingUtil.capitalize(table.getName()) :
                NamingUtil.toCamelCase(table.getName());

        if (isOneToOne) {
            LoggingUtility.debug("Building 1:1 relation from {} to {}",
                    table.getName(), candidateTable.getName());

            return Relation.builder()
                    .type(relationType)
                    .targetEntityClass(NamingUtil.toPascalCase(candidateTable.getName()))
                    .fieldName(NamingUtil.generateSelfRefFieldName(candidateTable.getName(), relationType, isSelfReferencing))
                    .fetchType(getFetchType())
                    .cascadeType(getCascadeType(relationType))
                    .orphanRemoval(getOrphanRemoval(relationType))
                    .mappedBy(mappedByField)
                    .selfReferencing(isSelfReferencing)
                    .build();
        } else {
            LoggingUtility.debug("Building N:1 relation from {} to {}",
                    table.getName(), candidateTable.getName());

            return Relation.builder()
                    .type(relationType)
                    .targetEntityClass(NamingUtil.toPascalCase(candidateTable.getName()))
                    .fieldName(NamingUtil.generateSelfRefFieldName(candidateTable.getName(), relationType, isSelfReferencing))
                    .fetchType(getFetchType())
                    .cascadeType(getCascadeType(relationType))
                    .orphanRemoval(getOrphanRemoval(relationType))
                    .collectionType(getCollectionType(fkGroup))
                    .mappedBy(mappedByField)
                    .selfReferencing(isSelfReferencing)
                    .build();
        }
    }

}
