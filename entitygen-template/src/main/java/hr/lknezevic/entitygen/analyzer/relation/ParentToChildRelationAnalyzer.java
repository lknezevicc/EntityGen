package hr.lknezevic.entitygen.analyzer.relation;

import hr.lknezevic.entitygen.analyzer.RelationBuilder;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.template.common.Relation;
import hr.lknezevic.entitygen.utils.NamingUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Builder for generating 1:1 or N:1 relations in an entity.
 * It identifies foreign keys that reference the parent table and creates appropriate relations.
 */
public class ParentToChildRelationAnalyzer extends AbstractRelationAnalyzer {

    public ParentToChildRelationAnalyzer(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    /**
     * Builds 1:1 or N:1 for the parent table based on its foreign keys.
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> buildSpecificRelations() {
        List<Relation> relations = new ArrayList<>();

        // Group foreign keys by constraint name
        Map<String, List<ForeignKey>> groupedFKs = RelationDetector.groupForeignKeysByConstraint(table.getForeignKeys());

        for (Map.Entry<String, List<ForeignKey>> entry : groupedFKs.entrySet()) {
            List<ForeignKey> fkGroup = entry.getValue();
            if (fkGroup.isEmpty()) continue;

            ForeignKey sample = fkGroup.getFirst();
            String targetTableName = sample.getReferencedTable();

            Relation relation = buildOwningRelation(fkGroup, targetTableName);

            String relationKey = RelationDetector.generateRelationKey(
                    table.getName(),
                    targetTableName,
                    relation.getType().name()
            );

            if (relationNotProcessed(relationKey)) {
                relations.add(relation);
                addProcessedRelation(relationKey);
            }
        }

        return relations;
    }

    private Relation buildOwningRelation(List<ForeignKey> fkGroup, String targetTableName) {
        ForeignKey sample = fkGroup.getFirst();

        boolean isOneToOne = RelationDetector.isOneToOneRelation(table, fkGroup);
        RelationType relationType = isOneToOne ? RelationType.ONE_TO_ONE : RelationType.MANY_TO_ONE;

        boolean isSelfReferencing = table.getName().equals(targetTableName);

        Relation.RelationBuilder builder = Relation.builder()
                .type(relationType)
                .targetEntityClass(NamingUtil.toPascalCase(targetTableName))
                .fieldName(NamingUtil.generateSelfRefFieldName(targetTableName, relationType, isSelfReferencing))
                .optional(isOptional(sample))
                .fetchType(getFetchType())
                .cascadeType(getCascadeType(relationType))
                .orphanRemoval(getOrphanRemoval(relationType))
                .selfReferencing(isSelfReferencing);

        List<String> joinColumns = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .filter(Objects::nonNull)
                .toList();

        List<String> referencedColumns = fkGroup.stream()
                .map(ForeignKey::getReferencedColumn)
                .filter(Objects::nonNull)
                .toList();

        if (!joinColumns.isEmpty() && joinColumns.size() == referencedColumns.size()) {
            builder.joinColumns(joinColumns)
                    .referencedColumns(referencedColumns);
        }

        if (isOneToOne) {
            if (RelationDetector.isForeignKeyEqualsPrimaryKey(table, fkGroup)) {
                String mapsId = joinColumns.size() == 1 ? joinColumns.getFirst() : null;
                builder.mapsId(mapsId);
            }

            if (sample.isOnDeleteCascade()) {
                builder.orphanRemoval(true);
            }
        }

        return builder.build();
    }
}
