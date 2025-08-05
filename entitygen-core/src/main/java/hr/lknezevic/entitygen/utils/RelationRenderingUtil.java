package hr.lknezevic.entitygen.utils;

import hr.lknezevic.entitygen.model.template.TemplateFactory;
import hr.lknezevic.entitygen.model.template.TemplateConst;
import hr.lknezevic.entitygen.model.template.common.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for building relation parameters
 */
public class RelationRenderingUtil {

    public static String buildManyToOneParams(Relation relation) {
        List<String> params = new ArrayList<>();
        
        addFetchType(params, relation);
        addCascadeType(params, relation);
        addOptional(params, relation);
        
        return TemplateUtil.formatOptionalParameters(params.toArray(new String[0]));
    }

    public static String buildOneToOneParams(Relation relation) {
        List<String> params = new ArrayList<>();
        
        addMappedBy(params, relation);

        if (relation.getMappedBy() == null) {
            addFetchType(params, relation);
        }

        addCascadeType(params, relation);
        addOptional(params, relation);
        
        addOrphanRemoval(params, relation);
        
        return TemplateUtil.formatOptionalParameters(params.toArray(new String[0]));
    }

    public static String buildOneToManyParams(Relation relation) {
        List<String> params = new ArrayList<>();
        
        addMappedBy(params, relation);
        addFetchType(params, relation);
        addCascadeType(params, relation);
        addOrphanRemoval(params, relation);
        
        return TemplateUtil.formatOptionalParameters(params.toArray(new String[0]));
    }

    public static String buildManyToManyParams(Relation relation) {
        List<String> params = new ArrayList<>();
        
        addMappedBy(params, relation);
        addFetchType(params, relation);
        addCascadeType(params, relation);
        
        return TemplateUtil.formatOptionalParameters(params.toArray(new String[0]));
    }

    private static void addMappedBy(List<String> params, Relation relation) {
        if (relation.getMappedBy() != null) {
            params.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_MAPPED_BY)
                    .build()
                    .addParam(relation.getMappedBy())
                    .format());
        }
    }

    private static void addFetchType(List<String> params, Relation relation) {
        if (relation.getFetchType() != null) {
            params.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_FETCH)
                    .build()
                    .addParam(relation.getFetchType())
                    .format());
        }
    }

    private static void addCascadeType(List<String> params, Relation relation) {
        params.add(TemplateFactory.builder()
                .template(TemplateConst.RELATION_PARAM_CASCADE)
                .build()
                .addParam(relation.getCascadeType().getValue())
                .format());
    }

    private static void addOptional(List<String> params, Relation relation) {
        if (relation.getOptional() != null && !relation.getOptional()) {
            params.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_OPTIONAL)
                    .build()
                    .addParam("false")
                    .format());
        }
    }

    private static void addOrphanRemoval(List<String> params, Relation relation) {
        if (relation.getOrphanRemoval() != null && relation.getOrphanRemoval()) {
            params.add(TemplateFactory.builder()
                    .template(TemplateConst.RELATION_PARAM_ORPHAN_REMOVAL)
                    .build()
                    .addParam("true")
                    .format());
        }
    }

}
