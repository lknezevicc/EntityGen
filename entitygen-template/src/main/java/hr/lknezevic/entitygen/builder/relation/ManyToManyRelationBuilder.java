package hr.lknezevic.entitygen.builder.relation;

import hr.lknezevic.entitygen.builder.RelationBuilder;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ManyToManyRelationBuilder extends AbstractRelationBuilder {

    public ManyToManyRelationBuilder(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    @Override
    public List<Relation> buildSpecificRelations() {
        List<Relation> relations = new ArrayList<>();

        for (Table candidateTable : getContext().getAllTables()) {
            if (!RelationDetector.isJunctionTable(candidateTable)) {
                continue;
            }

            List<ForeignKey> junctionFKs = candidateTable.getForeignKeys();
            if (junctionFKs.size() != 2) continue;

            ForeignKey fk1 = junctionFKs.get(0);
            ForeignKey fk2 = junctionFKs.get(1);

            // Provjeri da li naša tablica sudjeluje u ovoj many-to-many relaciji
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

            if (targetTableName == null) continue;

            // Provjeri da li postoji target entity
            Optional<Entity> targetEntityOpt = Optional.ofNullable(getContext().getEntityByTableName().get(targetTableName));
            if (targetEntityOpt.isEmpty()) continue;

            Entity targetEntity = targetEntityOpt.get();
            boolean hasAdditionalData = hasAdditionalDataColumns(candidateTable, junctionFKs);

            if (!hasAdditionalData) {
                // ✅ ČISTA JUNCTION TABLICA - Generiraj pravu @ManyToMany relaciju
                Relation relation = buildPureManyToManyRelation(candidateTable, targetEntity, currentTableFKs, targetTableFKs);

                String relationKey = RelationDetector.generateRelationKey(
                        table.getName(), targetTableName, RelationType.MANY_TO_MANY.name());

                if (!getProcessedRelations().contains(relationKey)) {
                    relations.add(relation);
                    addProcessedRelation(relationKey);
                    log.debug("Generated MANY_TO_MANY relation from {} to {} via junction table {}", 
                             table.getName(), targetTableName, candidateTable.getName());
                }
            } else {
                // ✅ JUNCTION TABLICA SA ADDITIONAL DATA
                // Junction tablica će biti tretirana kao regularni entitet
                // ONE_TO_MANY relacije prema association entity-u će biti generirane u drugim builderima
                log.debug("Junction table {} has additional data - skipping Many-to-Many generation, will be handled as association entity", 
                         candidateTable.getName());
            }
        }

        return relations;
    }

    /**
     * Provjeri da li junction tablica ima additional data kolone
     */
    private boolean hasAdditionalDataColumns(Table junctionTable, List<ForeignKey> foreignKeys) {
        Set<String> fkColumns = foreignKeys.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());

        Set<String> pkColumns = new HashSet<>(junctionTable.getPrimaryKeys());

        // Ukloni FK i PK kolone
        Set<String> additionalColumns = junctionTable.getColumns().stream()
                .map(Column::getName).collect(Collectors.toSet());
        additionalColumns.removeAll(fkColumns);
        additionalColumns.removeAll(pkColumns);

        return !additionalColumns.isEmpty();
    }

    /**
     * Kreira čistu MANY_TO_MANY relaciju za junction tablice bez additional data
     */
    private Relation buildPureManyToManyRelation(Table junctionTable, Entity targetEntity,
                                                 List<ForeignKey> currentFKs, List<ForeignKey> targetFKs) {

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
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(NamingUtil.generateFieldName(targetEntity.getClassName(), RelationType.MANY_TO_MANY, true))
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
}
