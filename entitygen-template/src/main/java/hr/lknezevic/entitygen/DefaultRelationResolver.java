package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.RelationType;
import hr.lknezevic.entitygen.model.ForeignKey;
import hr.lknezevic.entitygen.model.Table;
import hr.lknezevic.entitygen.model.template.Entity;
import hr.lknezevic.entitygen.model.template.Relation;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultRelationResolver implements RelationResolver {

    @Override
    public List<Relation> buildRelations(Table table, List<Table> allTables, List<Entity> allEntities) {
        List<Relation> result = new ArrayList<>();

        // 1. MANY_TO_ONE & ONE_TO_ONE
        Map<String, List<ForeignKey>> groupedFKs = table.getForeignKeys().stream()
                .collect(Collectors.groupingBy(ForeignKey::getName));

        for (Map.Entry<String, List<ForeignKey>> entry : groupedFKs.entrySet()) {
            List<ForeignKey> group = entry.getValue();
            ForeignKey sample = group.get(0);

            Optional<Entity> targetEntityOpt = findEntity(sample.getReferencedTable(), allEntities);
            if (targetEntityOpt.isEmpty()) continue;
            Entity targetEntity = targetEntityOpt.get();

            Relation relation = new Relation();
            boolean isComposite = group.size() > 1;

            boolean isFkPartOfPrimaryKey = table.getPrimaryKeys().contains(sample.getFkColumn());
            boolean fkEqualsPk = isForeignKeyEqualsPrimaryKey(table, group);

            if (fkEqualsPk) {
                relation.setType(RelationType.ONE_TO_ONE);

                // Ako je kompozitni PK, pridruži sve joinColumns kao mapsId
                String mapsId = group.stream()
                        .map(ForeignKey::getFkColumn)
                        .collect(Collectors.joining(", ")); // npr. "id" ili "posiljatelj_ime, posiljatelj_domena"

                relation.setMapsId(mapsId);
            } else {
                boolean isOneToOne = sample.isUnique();
                relation.setType(isOneToOne ? RelationType.ONE_TO_ONE : RelationType.MANY_TO_ONE);

                // orphanRemoval ima smisla samo za OneToOne
                if (isOneToOne && sample.isOnDeleteCascade()) {
                    relation.setOrphanRemoval(true);
                }
            }

// nastavak kao i do sada
            relation.setTargetEntityClass(targetEntity.getClassName());
            relation.setFieldName(toCamelCase(sample.getReferencedTable()));
            relation.setOptional(!sample.isNotNull());
            relation.setCascadeType(CascadeType.ALL);

            if (group.size() > 1) {
                relation.setJoinColumns(group.stream().map(ForeignKey::getFkColumn).toList());
                relation.setReferencedColumns(group.stream().map(ForeignKey::getReferencedColumn).toList());
            } else {
                relation.setJoinColumns(List.of(sample.getFkColumn()));
                relation.setReferencedColumns(List.of(sample.getReferencedColumn()));
            }

            if (isComposite) {
                relation.setJoinColumns(group.stream()
                        .map(ForeignKey::getFkColumn)
                        .toList());
                relation.setReferencedColumns(group.stream()
                        .map(ForeignKey::getReferencedColumn)
                        .toList());
            } else {
                relation.setJoinColumns(List.of(sample.getFkColumn()));
                relation.setReferencedColumns(List.of(sample.getReferencedColumn()));
            }

            result.add(relation);
        }

        // 2. Inverzna ONE_TO_MANY
        for (Entity possibleChild : allEntities) {
            for (Relation rel : possibleChild.getRelations()) {
                if (rel.getTargetEntityClass().equals(getEntityClassName(table.getName()))
                        && rel.getType() == RelationType.MANY_TO_ONE) {

                    Relation inverse = new Relation();
                    inverse.setType(RelationType.ONE_TO_MANY);
                    inverse.setTargetEntityClass(possibleChild.getClassName());
                    inverse.setFieldName(toCamelCase(possibleChild.getClassName()) + "List");
                    inverse.setMappedBy(rel.getFieldName());
                    inverse.setCascadeType(CascadeType.ALL);
                    inverse.setOrphanRemoval(false);
                    inverse.setCollectionType(CollectionType.LIST);
                    result.add(inverse);
                }
            }
        }

        // 3. MANY_TO_MANY
        for (Table candidate : allTables) {
            if (candidate.equals(table)) continue;

            List<ForeignKey> fks = candidate.getForeignKeys();
            if (fks.size() != 2) continue;
            if (candidate.getColumns().size() > 2) continue;

            ForeignKey fk1 = fks.get(0);
            ForeignKey fk2 = fks.get(1);

            boolean currentIsReferenced = fk1.getReferencedTable().equals(table.getName()) ||
                    fk2.getReferencedTable().equals(table.getName());
            if (!currentIsReferenced) continue;

            String otherTable = fk1.getReferencedTable().equals(table.getName()) ? fk2.getReferencedTable() : fk1.getReferencedTable();

            Optional<Entity> targetEntityOpt = findEntity(otherTable, allEntities);
            if (targetEntityOpt.isEmpty()) continue;
            Entity targetEntity = targetEntityOpt.get();

            Relation rel = new Relation();
            rel.setType(RelationType.MANY_TO_MANY);
            rel.setTargetEntityClass(targetEntity.getClassName());
            rel.setFieldName(toCamelCase(targetEntity.getClassName()) + "List");
            rel.setJoinTableName(candidate.getName());

            List<ForeignKey> currentFKs = fks.stream()
                    .filter(fk -> fk.getReferencedTable().equals(table.getName()))
                    .toList();
            List<ForeignKey> inverseFKs = fks.stream()
                    .filter(fk -> fk.getReferencedTable().equals(otherTable))
                    .toList();

            boolean isComposite = currentFKs.size() > 1 || inverseFKs.size() > 1;

            if (isComposite) {
                rel.setJoinColumns(currentFKs.stream()
                        .map(ForeignKey::getFkColumn)
                        .toList());
                rel.setReferencedColumns(currentFKs.stream()
                        .map(ForeignKey::getReferencedColumn)
                        .toList());

                rel.setInverseJoinColumns(inverseFKs.stream()
                        .map(ForeignKey::getFkColumn)
                        .toList());
                rel.setInverseReferencedColumns(inverseFKs.stream()
                        .map(ForeignKey::getReferencedColumn)
                        .toList());

            } else {
                rel.setJoinColumns(List.of(currentFKs.get(0).getFkColumn()));
                rel.setReferencedColumns(List.of(currentFKs.get(0).getReferencedColumn()));

                rel.setInverseJoinColumns(List.of(inverseFKs.get(0).getFkColumn()));
                rel.setInverseReferencedColumns(List.of(inverseFKs.get(0).getReferencedColumn()));
            }

            boolean useSet = currentFKs.stream().allMatch(ForeignKey::isUnique) &&
                    inverseFKs.stream().allMatch(ForeignKey::isUnique);

            rel.setCollectionType(useSet ? CollectionType.SET : CollectionType.LIST);
            rel.setCascadeType(CascadeType.ALL);

            result.add(rel);
        }

        return result;
    }

    private Optional<Entity> findEntity(String tableName, List<Entity> entities) {
        return entities.stream()
                .filter(e -> e.getTableName().equalsIgnoreCase(tableName))
                .findFirst();
    }

    private String getEntityClassName(String tableName) {
        if (tableName == null || tableName.isEmpty()) return tableName;
        return Character.toUpperCase(tableName.charAt(0)) + tableName.substring(1);
    }

    private String toCamelCase(String input) {
        if (input == null || input.isEmpty()) return input;
        return Character.toLowerCase(input.charAt(0)) + input.substring(1);
    }

    private boolean isForeignKeyEqualsPrimaryKey(Table table, List<ForeignKey> fkGroup) {
        Set<String> fkCols = fkGroup.stream()
                .map(ForeignKey::getFkColumn)
                .collect(Collectors.toSet());

        Set<String> pkCols = new HashSet<>(table.getPrimaryKeys());

        return pkCols.equals(fkCols);
    }
}