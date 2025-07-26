package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.helper.NamingHelper;
import hr.lknezevic.entitygen.helper.RelationDetector;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Poboljšana implementacija RelationResolver-a koja pruža robusniju detekciju relacija
 */
public class RelationBuilder {
    private Table table;




    public List<Relation> buildRelations(Table table, List<Table> allTables, List<Entity> allEntities) {
        List<Relation> result = new ArrayList<>();
        Set<String> processedRelations = new HashSet<>();

        // 1. Kreiraj MANY_TO_ONE i ONE_TO_ONE relacije (parent-to-child)
        result.addAll(buildParentToChildRelations(table, allEntities, processedRelations));

        // 2. Kreiraj inverse ONE_TO_ONE i ONE_TO_MANY relacije
        result.addAll(buildInverseRelations(table, allTables, allEntities, processedRelations));

        // 3. Kreiraj MANY_TO_MANY relacije
        result.addAll(buildManyToManyRelations(table, allTables, allEntities, processedRelations));

        return result;
    }

    /**
     * Kreira MANY_TO_ONE i ONE_TO_ONE relacije na temelju foreign key-ova
     */
    private List<Relation> buildParentToChildRelations(Table table, List<Entity> allEntities, Set<String> processedRelations) {
        List<Relation> relations = new ArrayList<>();

        // Grupiranje FK-ova po constraint name
        Map<String, List<ForeignKey>> groupedFKs = RelationDetector.groupForeignKeysByConstraint(table.getForeignKeys());

        for (Map.Entry<String, List<ForeignKey>> entry : groupedFKs.entrySet()) {
            List<ForeignKey> fkGroup = entry.getValue();
            
            // Validacija FK grupe
            if (fkGroup.isEmpty()) continue;
            
            ForeignKey sample = fkGroup.get(0);
            String targetTableName = sample.getReferencedTable();
            
            // Provjeri da li postoji target entity
            Optional<Entity> targetEntityOpt = RelationDetector.findEntityByTableName(targetTableName, allEntities);
            if (targetEntityOpt.isEmpty()) continue;
            
            Entity targetEntity = targetEntityOpt.get();
            
            // Kreiraj relation
            Relation relation = buildOwningRelation(table, fkGroup, targetEntity);
            
            // Provjeri duplikate
            String relationKey = RelationDetector.generateRelationKey(
                table.getName(), 
                targetTableName, 
                relation.getType().name()
            );
            
            if (!processedRelations.contains(relationKey)) {
                relations.add(relation);
                processedRelations.add(relationKey);
            }
        }

        return relations;
    }

    /**
     * Kreira owning stranu relacije (MANY_TO_ONE ili ONE_TO_ONE)
     */
    private Relation buildOwningRelation(Table sourceTable, List<ForeignKey> fkGroup, Entity targetEntity) {
        ForeignKey sample = fkGroup.get(0);
        
        // Determiniraj tip relacije
        boolean isOneToOne = RelationDetector.isOneToOneRelation(sourceTable, fkGroup);
        RelationType relationType = isOneToOne ? RelationType.ONE_TO_ONE : RelationType.MANY_TO_ONE;
        
        // Kreiraj relation objekt
        Relation.RelationBuilder builder = Relation.builder()
                .type(relationType)
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(NamingHelper.generateFieldName(targetEntity.getClassName(), relationType.name(), false))
                .optional(!sample.isNotNull())
                .fetchType(FetchType.LAZY)
                .cascadeType(determineCascadeType(sample, isOneToOne))
                .orphanRemoval(false); // Default false

        // Join columns mapping
        List<String> joinColumns = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        List<String> referencedColumns = fkGroup.stream()
                .map(ForeignKey::getReferencedColumn)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (!joinColumns.isEmpty() && joinColumns.size() == referencedColumns.size()) {
            builder.joinColumns(joinColumns)
                   .referencedColumns(referencedColumns);
        }

        // Posebno za ONE_TO_ONE relacije
        if (isOneToOne) {
            // Shared primary key pattern
            if (RelationDetector.isForeignKeyEqualsPrimaryKey(sourceTable, fkGroup)) {
                String mapsId = joinColumns.size() == 1 ? joinColumns.get(0) : null;
                builder.mapsId(mapsId);
            }
            
            // Orphan removal za cascade delete
            if (sample.isOnDeleteCascade()) {
                builder.orphanRemoval(true);
            }
        }

        return builder.build();
    }

    /**
     * Kreira MANY_TO_MANY relacije kroz junction tablice
     */
    private List<Relation> buildManyToManyRelations(Table table, List<Table> allTables, List<Entity> allEntities, Set<String> processedRelations) {
        List<Relation> relations = new ArrayList<>();

        for (Table candidateTable : allTables) {
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
            Optional<Entity> targetEntityOpt = RelationDetector.findEntityByTableName(targetTableName, allEntities);
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

                if (!processedRelations.contains(relationKey)) {
                    relations.add(relation);
                    processedRelations.add(relationKey);
                }
            }
            // Ako ima additional data, ostaviti ONE-TO-MANY prema junction entity-u
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
        Set<String> allColumns = junctionTable.getColumns().stream()
                .map(column -> column.getName())
                .collect(Collectors.toSet());
        
        // Ukloni FK i PK kolone
        Set<String> additionalColumns = new HashSet<>(allColumns);
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

        // Determiniraj collection type (SET ako su svi FK unique, inače LIST)
        boolean useSet = currentFKs.stream().allMatch(ForeignKey::isUnique) && 
                        targetFKs.stream().allMatch(ForeignKey::isUnique);

        return Relation.builder()
                .type(RelationType.MANY_TO_MANY)
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(NamingHelper.generateFieldName(targetEntity.getClassName(), RelationType.MANY_TO_MANY.name(), true))
                .fetchType(FetchType.LAZY)
                .cascadeType(CascadeType.ALL)
                .orphanRemoval(false) // Default false for MANY_TO_MANY
                .collectionType(useSet ? CollectionType.SET : CollectionType.LIST)
                .joinTableName(junctionTable.getName())
                .joinColumns(joinColumns)
                .referencedColumns(joinReferencedColumns)
                .inverseJoinColumns(inverseJoinColumns)
                .inverseReferencedColumns(inverseReferencedColumns)
                .build();
    }

    /**
     * Determiniraj cascade type na temelju database constrainta
     */
    private CascadeType determineCascadeType(ForeignKey foreignKey, boolean isOneToOne) {
        if (foreignKey.isOnDeleteCascade() || foreignKey.isOnUpdateCascade()) {
            return CascadeType.ALL;
        }
        
        // Za ONE_TO_ONE relacije češće koristimo ALL cascade
        if (isOneToOne) {
            return CascadeType.ALL;
        }
        
        // Za MANY_TO_ONE defaultno PERSIST i MERGE
        return CascadeType.PERSIST;
    }

    /**
     * Kreira inverse relacije (ONE_TO_ONE mappedBy i ONE_TO_MANY)
     */
    private List<Relation> buildInverseRelations(Table table, List<Table> allTables, List<Entity> allEntities, Set<String> processedRelations) {
        List<Relation> relations = new ArrayList<>();

        // Pronađi tablice koje referenciraju trenutnu tablicu
        for (Table otherTable : allTables) {
            if (otherTable.getName().equals(table.getName())) continue;

            List<ForeignKey> referencingFKs = otherTable.getForeignKeys().stream()
                    .filter(fk -> fk.getReferencedTable().equals(table.getName()))
                    .collect(Collectors.toList());

            if (referencingFKs.isEmpty()) continue;

            // Pronađi entity za other table
            Optional<Entity> otherEntityOpt = RelationDetector.findEntityByTableName(otherTable.getName(), allEntities);
            if (otherEntityOpt.isEmpty()) continue;

            Entity otherEntity = otherEntityOpt.get();

            // Grupiranje FK-ova po constraint name
            Map<String, List<ForeignKey>> groupedFKs = RelationDetector.groupForeignKeysByConstraint(referencingFKs);

            for (Map.Entry<String, List<ForeignKey>> entry : groupedFKs.entrySet()) {
                List<ForeignKey> fkGroup = entry.getValue();
                
                // Kreiraj inverse relaciju
                Relation inverseRelation = buildInverseRelation(table, otherTable, otherEntity, fkGroup);
                
                if (inverseRelation != null) {
                    // Provjeri duplikate
                    String relationKey = RelationDetector.generateRelationKey(
                        table.getName(), 
                        otherTable.getName(), 
                        inverseRelation.getType().name() + "_INVERSE"
                    );
                    
                    if (!processedRelations.contains(relationKey)) {
                        relations.add(inverseRelation);
                        processedRelations.add(relationKey);
                    }
                }
            }
        }

        return relations;
    }

    /**
     * Kreira inverse relaciju (ONE_TO_ONE mappedBy ili ONE_TO_MANY)
     */
    private Relation buildInverseRelation(Table currentTable, Table referencingTable, Entity referencingEntity, List<ForeignKey> fkGroup) {
        // Determiniraj tip inverse relacije
        boolean isOneToOne = RelationDetector.isOneToOneRelation(referencingTable, fkGroup);
        
        if (isOneToOne) {
            // Kreiraj ONE_TO_ONE mappedBy relaciju
            // mappedBy polje je ime polja u referencingEntity koje pokazuje na currentTable
            String mappedByField = NamingHelper.toCamelCase(currentTable.getName());
            
            return Relation.builder()
                    .type(RelationType.ONE_TO_ONE)
                    .targetEntityClass(referencingEntity.getClassName())
                    .fieldName(NamingHelper.toCamelCase(referencingEntity.getClassName()))
                    .fetchType(FetchType.LAZY)
                    .cascadeType(CascadeType.ALL)
                    .orphanRemoval(false)
                    .mappedBy(mappedByField)
                    .build();
        } else {
            // Kreiraj ONE_TO_MANY relaciju
            // mappedBy polje je ime polja u referencingEntity koje pokazuje na currentTable
            String mappedByField = NamingHelper.toCamelCase(currentTable.getName());
            
            return Relation.builder()
                    .type(RelationType.ONE_TO_MANY)
                    .targetEntityClass(referencingEntity.getClassName())
                    .fieldName(NamingHelper.toCamelCase(referencingEntity.getClassName()) + "List")
                    .fetchType(FetchType.LAZY)
                    .cascadeType(CascadeType.ALL)
                    .orphanRemoval(true) // ONE_TO_MANY typically true
                    .collectionType(CollectionType.LIST)
                    .mappedBy(mappedByField)
                    .build();
        }
    }
}