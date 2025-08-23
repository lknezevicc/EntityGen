package hr.lknezevic.entitygen.analyzer.relation;

import hr.lknezevic.entitygen.analyzer.RelationBuilder;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.domain.Relation;
import hr.lknezevic.entitygen.utils.LoggingUtility;
import hr.lknezevic.entitygen.utils.NamingUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Builder for generating M:N relations in an entity.
 * It identifies junction tables and creates appropriate relations.
 */
public class ManyToManyRelationAnalyzer extends AbstractRelationAnalyzer {

    public ManyToManyRelationAnalyzer(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    /**
     * Builds M:N relations based on junction tables that reference the current table.
     *
     * @return a list of relations
     */
    @Override
    public List<Relation> buildSpecificRelations() {
        List<Relation> relations = new ArrayList<>();

        // Iterating through all tables to find junction tables
        for (Table junctionTableCandidate : getContext().getAllTables()) {
            if (!RelationDetector.isJunctionTable(junctionTableCandidate)) continue;

            List<ForeignKey> junctionFKs = junctionTableCandidate.getForeignKeys();
            // If it doesn't have exactly two foreign keys, it's not a valid junction table
            if (junctionFKs.size() != 2) continue;

            ForeignKey fk1 = junctionFKs.get(0);
            ForeignKey fk2 = junctionFKs.get(1);

            // Check if the current table is referenced by either of the foreign keys
            String targetTableName = null;
            List<ForeignKey> currentTableFKs = new ArrayList<>();
            List<ForeignKey> targetTableFKs = new ArrayList<>();

            if (fk1.getReferencedTable().equals(table.getName())) {
                targetTableName = fk2.getReferencedTable();
                currentTableFKs.add(fk1);
                targetTableFKs.add(fk2);
            } else if (fk2.getReferencedTable().equals(table.getName())) {
                targetTableName = fk1.getReferencedTable();
                currentTableFKs.add(fk2);
                targetTableFKs.add(fk1);
            }

            // If the target table name is still null, it means the current table is not part of this junction table
            if (targetTableName == null) continue;

            // Check if junction table has additional data columns
            boolean hasAdditionalData = hasAdditionalDataColumns(junctionTableCandidate, junctionFKs);
            if (hasAdditionalData) {
                LoggingUtility.debug("Junction table {} has additional data - treating as association entity",
                        junctionTableCandidate.getName());
                continue;
            }

            // Check if the current table is the owner of the relation
            boolean isOwner = table.getName().compareTo(targetTableName) <= 0;

            if (isOwner) {
                // Build owner relation
                Relation ownerRelation = buildOwnerManyToManyRelation(junctionTableCandidate, targetTableName, currentTableFKs, targetTableFKs);

                String relationKey = RelationDetector.generateRelationKey(
                        table.getName(), targetTableName, RelationType.MANY_TO_MANY.name());

                if (relationNotProcessed(relationKey)) {
                    relations.add(ownerRelation);
                    addProcessedRelation(relationKey);
                    LoggingUtility.debug("Generated M:N relation from {} to {} via junction table {}",
                            table.getName(), targetTableName, junctionTableCandidate.getName());
                }
            } else {
                Relation inverseRelation = buildInverseManyToManyRelation(targetTableName);

                String relationKey = RelationDetector.generateRelationKey(
                        table.getName(), targetTableName, RelationType.MANY_TO_MANY.name() + "_INVERSE");

                if (relationNotProcessed(relationKey)) {
                    relations.add(inverseRelation);
                    addProcessedRelation(relationKey);
                    LoggingUtility.debug("Generated INVERSE M:N relation from {} to {}",
                            table.getName(), targetTableName);
                }
            }
        }

        return relations;
    }

    private boolean hasAdditionalDataColumns(Table junctionTable, List<ForeignKey> foreignKeys) {
        Set<String> fkColumns = foreignKeys.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());

        Set<String> pkColumns = new HashSet<>(junctionTable.getPrimaryKeys());

        Set<String> additionalColumns = junctionTable.getColumns().stream()
                .map(Column::getName).collect(Collectors.toSet());
        additionalColumns.removeAll(fkColumns);
        additionalColumns.removeAll(pkColumns);

        return !additionalColumns.isEmpty();
    }

    private Relation buildOwnerManyToManyRelation(Table junctionTable, String targetTableName,
                                                  List<ForeignKey> currentFKs, List<ForeignKey> targetFKs) {

        String fieldName = table.getName().equals(targetTableName) ?
                NamingUtil.generateSelfRefFieldName(targetTableName, RelationType.MANY_TO_MANY, false) :
                NamingUtil.generateFieldName(targetTableName, RelationType.MANY_TO_MANY, true);

        List<String> joinColumns = currentFKs.stream()
                .map(ForeignKey::getFkColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> joinReferencedColumns = currentFKs.stream()
                .map(ForeignKey::getReferencedColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> inverseJoinColumns = targetFKs.stream()
                .map(ForeignKey::getFkColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> inverseReferencedColumns = targetFKs.stream()
                .map(ForeignKey::getReferencedColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return Relation.builder()
                .type(RelationType.MANY_TO_MANY)
                .targetEntityClass(NamingUtil.toPascalCase(targetTableName))
                .fieldName(fieldName)
                .fetchType(getFetchType())
                .cascadeType(getCascadeType(RelationType.MANY_TO_MANY))
                .orphanRemoval(getOrphanRemoval(RelationType.MANY_TO_MANY))
                .collectionType(CollectionType.LINKED_HASH_SET)
                .joinTableName(junctionTable.getName())
                .joinColumns(joinColumns)
                .referencedColumns(joinReferencedColumns)
                .inverseJoinColumns(inverseJoinColumns)
                .inverseReferencedColumns(inverseReferencedColumns)
                .build();
    }

    private Relation buildInverseManyToManyRelation(String targetTableName) {
        boolean isSelfReferencing = table.getName().equals(targetTableName);

        String fieldName;
        if (isSelfReferencing) {
            fieldName = NamingUtil.generateSelfRefFieldName(targetTableName, RelationType.MANY_TO_MANY, true);
        } else {
            fieldName = NamingUtil.generateFieldName(targetTableName, RelationType.MANY_TO_MANY, true);
        }

        String mappedByField = table.getName().equals(targetTableName) ?
                NamingUtil.generateSelfRefFieldName(targetTableName, RelationType.MANY_TO_MANY, false) :
                NamingUtil.generateFieldName(table.getName(), RelationType.MANY_TO_MANY, true);

        return Relation.builder()
                .type(RelationType.MANY_TO_MANY)
                .targetEntityClass(NamingUtil.toPascalCase(targetTableName))
                .fieldName(fieldName)
                .fetchType(getFetchType())
                .cascadeType(getCascadeType(RelationType.MANY_TO_MANY))
                .orphanRemoval(getOrphanRemoval(RelationType.MANY_TO_MANY))
                .collectionType(CollectionType.LINKED_HASH_SET)
                .mappedBy(mappedByField)
                .selfReferencing(isSelfReferencing)
                .build();
    }

}
