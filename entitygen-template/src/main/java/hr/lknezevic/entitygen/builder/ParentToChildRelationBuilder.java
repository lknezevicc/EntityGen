package hr.lknezevic.entitygen.builder;

import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.utils.NamingUtil;
import hr.lknezevic.entitygen.helper.relation.RelationConfigHelper;
import hr.lknezevic.entitygen.helper.relation.RelationDetector;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.common.Entity;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.*;
import java.util.stream.Collectors;

public class ParentToChildRelationBuilder extends AbstractRelationBuilder {

    public ParentToChildRelationBuilder(RelationBuilder relationBuilder) {
        super(relationBuilder);
    }

    @Override
    protected List<Relation> buildSpecificRelations() {
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
            Optional<Entity> targetEntityOpt = Optional.ofNullable(getContext().getEntityByTableName().get(targetTableName));
            //Optional<Entity> targetEntityOpt = RelationDetector.findEntityByTableName(targetTableName, getContext().getAllEntities());
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

            if (!getProcessedRelations().contains(relationKey)) {
                relations.add(relation);
                addProcessedRelation(relationKey);
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

        // Provjeri da li je self-referencing
        boolean isSelfReferencing = sourceTable.getName().equals(sample.getReferencedTable());

        // Kreiraj relation objekt
        Relation.RelationBuilder builder = Relation.builder()
                .type(relationType)
                .targetEntityClass(targetEntity.getClassName())
                .fieldName(generateSelfReferencingFieldName(targetEntity.getClassName(), relationType, isSelfReferencing))
                .optional(!sample.isNotNull())
                .fetchType(getFetchType())
                .cascadeType(RelationConfigHelper.getCascadeTypeFromConstraints(sample, relationType))
                .orphanRemoval(getOrphanRemoval(relationType))
                .selfReferencing(isSelfReferencing);

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

            // Orphan removal za cascade delete constraint
            if (sample.isOnDeleteCascade()) {
                builder.orphanRemoval(true);
            }
        }

        return builder.build();
    }

    /**
     * Generiraj pametan naziv polja za self-referencing relacije
     */
    private String generateSelfReferencingFieldName(String targetEntityClass, RelationType relationType, boolean isSelfReferencing) {
        if (!isSelfReferencing) {
            return NamingUtil.generateFieldName(targetEntityClass, relationType, false);
        }

        // Za self-referencing relacije, koristi smislene nazive
        return switch (relationType) {
            case MANY_TO_ONE -> "parent" + targetEntityClass; // npr. parentKategorija
            case ONE_TO_ONE -> "related" + targetEntityClass;  // npr. relatedUser
            default -> NamingUtil.generateFieldName(targetEntityClass, relationType, false);
        };
    }
}
