package hr.lknezevic.entitygen.config;

import hr.lknezevic.entitygen.enums.CascadeType;
import hr.lknezevic.entitygen.enums.CollectionType;
import hr.lknezevic.entitygen.enums.FetchType;
import lombok.Builder;
import lombok.Data;

/**
 * Konfiguracija za RelationResolver
 */
@Data
@Builder
public class RelationResolverConfig {
    
    @Builder.Default
    private boolean generateBidirectionalRelations = true;
    
    @Builder.Default
    private FetchType defaultFetchType = FetchType.LAZY;
    
    @Builder.Default
    private CascadeType defaultCascadeType = CascadeType.ALL;
    
    @Builder.Default
    private CollectionType defaultCollectionType = CollectionType.LIST;
    
    @Builder.Default
    private boolean useOrphanRemoval = false;
    
    @Builder.Default
    private boolean preferSetForUniqueConstraints = true;
    
    @Builder.Default
    private boolean skipSystemTables = true;
    
    /**
     * Prefiksi tablica koje se smatraju junction tablicama
     */
    @Builder.Default
    private String[] junctionTablePrefixes = {"rel_", "map_", "link_"};
    
    /**
     * Sufiksi tablica koje se smatraju junction tablicama
     */
    @Builder.Default
    private String[] junctionTableSuffixes = {"_rel", "_map", "_link"};
    
    public static RelationResolverConfig getDefault() {
        return RelationResolverConfig.builder().build();
    }
}
