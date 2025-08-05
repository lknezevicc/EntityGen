package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.common.Field;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for building JPA annotations for relations
 */
public class AnnotationUtil {

    public static String buildMapsId(Relation relation) {
        if (relation.getMapsId() == null) return "";
        
        if (relation.getMapsId().isEmpty()) {
            return TemplateConst.MAPS_ID;
        } else {
            return TemplateFactory.builder()
                    .template(TemplateConst.MAPS_ID_WITH_VALUE)
                    .build()
                    .addParam(relation.getMapsId())
                    .format();
        }
    }

    public static String buildJoinColumn(String columnName, String referencedColumn, List<String> additionalParams) {
        String additionalParamsStr = additionalParams.isEmpty() ? "" : ", " + String.join(", ", additionalParams);
        
        return TemplateFactory.builder()
                .template(TemplateConst.JOIN_COLUMN_SINGLE)
                .build()
                .addParam(columnName)
                .addParam(referencedColumn)
                .addParam(additionalParamsStr)
                .format();
    }

    public static String buildJoinColumns(List<String> columns, List<String> referencedColumns,
                                          boolean hasCompositeKey, List<String> additionalParams) {
        if (columns.isEmpty()) return "";

        if (hasCompositeKey) {
            additionalParams.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_UPDATABLE_INSERTABLE)
                    .build()
                    .addParams(false, false)
                    .format()
            );
        }

        if (columns.size() == 1) {
            String referencedColumn = referencedColumns.isEmpty() ? 
                    TemplateConst.DEFAULT_REFERENCED_COLUMN : referencedColumns.getFirst();
            
            return buildJoinColumn(columns.getFirst(), referencedColumn, additionalParams);
        } else {
            List<String> items = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                String referencedColumn = i < referencedColumns.size() ? 
                        referencedColumns.get(i) : TemplateConst.DEFAULT_REFERENCED_COLUMN;
                
                items.add(buildJoinColumn(columns.get(i), referencedColumn, additionalParams));
            }
            
            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMNS_MULTIPLE)
                    .build()
                    .addParam(TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE, items.toArray(new String[0])))
                    .format();
        }
    }

    public static String buildJoinTableColumns(List<String> columns, List<String> referencedColumns) {
        if (columns.isEmpty()) return "";
        
        if (columns.size() == 1) {
            String referencedColumn = referencedColumns.isEmpty() ? 
                    TemplateConst.DEFAULT_REFERENCED_COLUMN : referencedColumns.getFirst();
            
            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMN_SINGLE)
                    .build()
                    .addParam(columns.getFirst())
                    .addParam(referencedColumn)
                    .addParam("")
                    .format();
        } else {
            List<String> items = new ArrayList<>();
            for (int i = 0; i < columns.size(); i++) {
                String referencedColumn = i < referencedColumns.size() ? 
                        referencedColumns.get(i) : TemplateConst.DEFAULT_REFERENCED_COLUMN;
                
                items.add(TemplateFactory.builder()
                        .template(TemplateConst.JOIN_COLUMN_SINGLE)
                        .build()
                        .addParam(columns.get(i))
                        .addParam(referencedColumn)
                        .addParam("")
                        .format());
            }
            
            return TemplateFactory.builder()
                    .template(TemplateConst.JOIN_COLUMNS_MULTIPLE)
                    .build()
                    .addParam(TemplateUtil.joinParams(TemplateConst.COMMA_JOIN_NEWLINE, items.toArray(new String[0])))
                    .format();
        }
    }

    /**
     * Builds JoinTable annotation with join columns and inverse join columns
     */
    public static String buildJoinTable(Relation relation) {
        if (relation.getJoinTableName() == null) return "";

        String joinColumns = buildJoinTableColumns(relation.getJoinColumns(), relation.getReferencedColumns());

        String inverseJoinColumns = buildJoinTableColumns(relation.getInverseJoinColumns(), relation.getInverseReferencedColumns());
        
        return TemplateFactory.builder()
                .template(TemplateConst.JOIN_TABLE)
                .build()
                .addParam(relation.getJoinTableName())
                .addParam(joinColumns)
                .addParam(inverseJoinColumns)
                .format();
    }

    public static String buildColumnParams(Field field) {
        List<String> params = new ArrayList<>();

        params.add(
                TemplateFactory.builder()
                        .template(TemplateConst.COLUMN_PARAM_NAME)
                        .build().addParam(field.getColumnName())
                        .format()
        );

        if (!field.isNullable()) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_NULLABLE)
                            .build().addParam(false)
                            .format()
            );
        }

        if (field.isUnique()) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_UNIQUE)
                            .build().addParam(true)
                            .format()
            );
        }

        if (!field.isLob() && field.getLength() != null && field.getLength() <= 255) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_LENGTH)
                            .build().addParam(field.getLength())
                            .format()
            );
        }

        if (!field.isLob() && field.getLength() != null && field.getLength() == 65535) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_LENGTH_LARGE)
                            .build()
                            .format()
            );
        }

        if (field.getPrecision() != null && field.getPrecision() > 0) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_PRECISION)
                            .build()
                            .addParam(field.getPrecision())
                            .format()
            );
        }

        if (field.getScale() != null && field.getScale() > 0) {
            params.add(
                    TemplateFactory.builder()
                            .template(TemplateConst.COLUMN_PARAM_SCALE)
                            .build()
                            .addParam(field.getScale())
                            .format()
            );
        }

        return TemplateUtil.joinParams(TemplateConst.COMMA_JOIN, params.toArray(new String[0]));
    }
}
