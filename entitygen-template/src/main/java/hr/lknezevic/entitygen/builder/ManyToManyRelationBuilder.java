package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.NamingHelper;
import hr.lknezevic.entitygen.helper.RelationDetector;
import hr.lknezevic.entitygen.model.Column;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.*;
import java.util.stream.Collectors;

public class ManyToManyRelationBuilder extends AbstractRelationBuilder {

    public ManyToManyRelationBuilder(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    @Override
    protected List<Relation> buildSpecificRelations() {
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
            //Optional<Entity> targetEntityOpt = RelationDetector.findEntityByTableName(targetTableName, getContext().getAllEntities());
            if (targetEntityOpt.isEmpty()) continue;

            Entity targetEntity = targetEntityOpt.get();

            // Generiraj MANY-TO-MANY samo ako junction nema additional data ili korisnik želi direktnu MANY-TO-MANY
            boolean hasAdditionalData = hasAdditionalDataColumns(candidateTable, junctionFKs);

            if (!hasAdditionalData) {
                // Kreiraj pravu MANY_TO_MANY relaciju
                Relation relation = buildManyToManyRelation(table, candidateTable, targetEntity, currentTableFKs, targetTableFKs);

                // Provjeri duplikate
                String relationKey = RelationDetector.generateRelationKey(
                        table.getName(),
                        targetTableName,
                        RelationType.MANY_TO_MANY.name()
                );

                if (!getProcessedRelations().contains(relationKey)) {
                    relations.add(relation);
                    addProcessedRelation(relationKey);
                }
            } else {
                // Ako ima additional data, kreiraj direktnu MANY_TO_MANY kao opciju
                // Ovo omogućuje korisnicima da imaju direktnu vezu bez association entiteta
                Relation directRelation = buildDirectManyToManyRelation(table, candidateTable, targetEntity, currentTableFKs, targetTableFKs);

                String directRelationKey = RelationDetector.generateRelationKey(
                        table.getName(),
                        targetTableName,
                        RelationType.MANY_TO_MANY_DIRECT.name()
                );

                if (!getProcessedRelations().contains(directRelationKey)) {
                    relations.add(directRelation);
                    addProcessedRelation(directRelationKey);
                }
            }
            // Također ostavi ONE-TO-MANY prema junction entity-u za additional data
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
     * Kreira MANY_TO_MANY relaciju
     */
    private Relation buildManyToManyRelation(Table sourceTable, Table junctionTable, Entity targetEntity,
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

        // Determiniraj collection type - koristi config override ili pametnu logiku
        CollectionType resolvedCollectionType = getCollectionType(junctionTable.getName(), currentFKs);
        
        // Dodatna provjera za unique constrainte - ako su svi FK unique, koristi SET
        if (resolvedCollectionType == CollectionType.LIST) {
            boolean allFKsUnique = currentFKs.stream().allMatch(ForeignKey::isUnique) && 
                                  targetFKs.stream().allMatch(ForeignKey::isUnique);
            if (allFKsUnique) {
                resolvedCollectionType = CollectionType.SET;
            }
        }

        return Relation.builder()
                .type(RelationType.MANY_TO_MANY)
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(NamingHelper.generateFieldName(targetEntity.getClassName(), RelationType.MANY_TO_MANY, true))
                .fetchType(getFetchType())
                .cascadeType(getCascadeType(RelationType.MANY_TO_MANY))
                .orphanRemoval(getOrphanRemoval(RelationType.MANY_TO_MANY))
                .collectionType(resolvedCollectionType)
                .joinTableName(junctionTable.getName())
                .joinColumns(joinColumns)
                .referencedColumns(joinReferencedColumns)
                .inverseJoinColumns(inverseJoinColumns)
                .inverseReferencedColumns(inverseReferencedColumns)
                .build();
    }

    /**
     * Kreira direktnu MANY_TO_MANY relaciju (bypasses association entity)
     */
    private Relation buildDirectManyToManyRelation(Table sourceTable, Table junctionTable, Entity targetEntity,
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
                .type(RelationType.MANY_TO_MANY_DIRECT)
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(NamingHelper.generateFieldName(targetEntity.getClassName(), RelationType.MANY_TO_MANY_DIRECT, true))
                .fetchType(getFetchType())
                .cascadeType(getCascadeType(RelationType.MANY_TO_MANY_DIRECT))
                .orphanRemoval(getOrphanRemoval(RelationType.MANY_TO_MANY_DIRECT))
                .collectionType(CollectionType.SET) // Always use SET for direct relationships
                .joinTableName(junctionTable.getName())
                .joinColumns(joinColumns)
                .referencedColumns(joinReferencedColumns)
                .inverseJoinColumns(inverseJoinColumns)
                .inverseReferencedColumns(inverseReferencedColumns)
                .isDirectManyToMany(true)
                .build();
    }
}
