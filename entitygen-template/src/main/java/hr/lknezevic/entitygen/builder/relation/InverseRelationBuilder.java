package hr.lknezevic.entitygen.builder.relation;

import hr.lknezevic.entitygen.builder.RelationBuilder;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InverseRelationBuilder extends AbstractRelationBuilder {

    public InverseRelationBuilder(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    /**
     * Kreira MANY_TO_ONE i ONE_TO_ONE relacije na temelju foreign key-ova
     */
    @Override
    public List<Relation> buildSpecificRelations() {
        List<Relation> relations = new ArrayList<>();

        // Pronađi tablice koje referenciraju trenutnu tablicu
        for (Table otherTable : getContext().getAllTables()) {
            List<ForeignKey> referencingFKs = otherTable.getForeignKeys().stream()
                    .filter(fk -> fk.getReferencedTable().equals(table.getName()))
                    .toList();

            if (referencingFKs.isEmpty()) continue;

            Optional<Entity> otherEntityOpt = Optional.ofNullable(getContext().getEntityByTableName().get(otherTable.getName()));
            if (otherEntityOpt.isEmpty()) continue;

            Entity otherEntity = otherEntityOpt.get();

            // Provjeri da li je self-referencing
            boolean isSelfReferencing = otherTable.getName().equals(table.getName());

            // Grupiranje FK-ova po constraint name
            Map<String, List<ForeignKey>> groupedFKs = RelationDetector.groupForeignKeysByConstraint(referencingFKs);

            for (var entry : groupedFKs.entrySet()) {
                List<ForeignKey> fkGroup = entry.getValue();
                
                // Kreiraj inverse relaciju
                Relation inverseRelation = buildInverseRelation(table, otherTable, otherEntity, fkGroup, isSelfReferencing);
                
                if (inverseRelation != null) {
                    // Provjeri duplikate
                    String relationKey = RelationDetector.generateRelationKey(
                        table.getName(), 
                        otherTable.getName(), 
                        inverseRelation.getType().name() + "_INVERSE"
                    );
                    
                    if (!isRelationProcessed(relationKey)) {
                        relations.add(inverseRelation);
                        addProcessedRelation(relationKey);
                    }
                }
            }
        }

        return relations;
    }

    /**
     * Kreira inverse relaciju (ONE_TO_ONE mappedBy ili ONE_TO_MANY)
     */
    private Relation buildInverseRelation(Table currentTable, Table referencingTable, Entity referencingEntity, List<ForeignKey> fkGroup, boolean isSelfReferencing) {
        // Determiniraj tip inverse relacije
        boolean isOneToOne = RelationDetector.isOneToOneRelation(referencingTable, fkGroup);
        String mappedByField = isSelfReferencing ? "parent" + NamingUtil.capitalize(currentTable.getName()) : NamingUtil.toCamelCase(currentTable.getName());

        if (isOneToOne) {
            // Kreiraj ONE_TO_ONE mappedBy relaciju
            // mappedBy polje je ime polja u referencingEntity koje pokazuje na currentTable

            return Relation.builder()
                    .type(RelationType.ONE_TO_ONE)
                    .targetEntityClass(referencingEntity.getClassName())
                    .fieldName(generateSelfReferencingInverseFieldName(referencingEntity.getClassName(), RelationType.ONE_TO_ONE, isSelfReferencing))
                    .fetchType(getFetchType())
                    .cascadeType(getCascadeType(RelationType.ONE_TO_ONE))
                    .orphanRemoval(getOrphanRemoval(RelationType.ONE_TO_ONE))
                    .mappedBy(mappedByField)
                    .selfReferencing(isSelfReferencing)
                    .build();
        } else {
            // Kreiraj ONE_TO_MANY relaciju
            // mappedBy polje je ime polja u referencingEntity koje pokazuje na currentTable

            return Relation.builder()
                    .type(RelationType.ONE_TO_MANY)
                    .targetEntityClass(referencingEntity.getClassName())
                    .fieldName(generateSelfReferencingInverseFieldName(referencingEntity.getClassName(), RelationType.ONE_TO_MANY, isSelfReferencing))
                    .fetchType(getFetchType())
                    .cascadeType(getCascadeType(RelationType.ONE_TO_MANY))
                    .orphanRemoval(getOrphanRemoval(RelationType.ONE_TO_MANY))
                    .collectionType(getCollectionType(fkGroup))
                    .mappedBy(mappedByField)
                    .selfReferencing(isSelfReferencing)
                    .build();
        }
    }

    /**
     * Generiraj pametan naziv polja za self-referencing inverse relacije
     */
    private String generateSelfReferencingInverseFieldName(String targetEntityClass, RelationType relationType, boolean isSelfReferencing) {
        if (!isSelfReferencing) {
            return NamingUtil.generateFieldName(targetEntityClass, relationType, relationType == RelationType.ONE_TO_MANY);
        }

        // Za self-referencing relacije, koristi smislene nazive
        return switch (relationType) {
            case ONE_TO_MANY -> "children";
            case ONE_TO_ONE -> "child" + targetEntityClass;
            default -> NamingUtil.generateFieldName(targetEntityClass, relationType, true);
        };
    }
}
