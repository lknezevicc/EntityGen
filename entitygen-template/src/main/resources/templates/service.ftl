package ${cfg.servicePackage};

import ${cfg.dtoPackage}.${entity.className}${cfg.dtoSuffix};
import java.util.List;

/**
 * Service interface for {@link ${cfg.entityPackage}.${entity.className}} business logic.
 * <p>
 * Provides high-level operations for ${entity.tableName} entity management.
 *
 * @author EntityGen Maven Plugin
 */
public interface ${entity.className}${cfg.serviceSuffix} {
    
    /**
     * Retrieves all entities as DTOs.
     *
     * @return list of all entities converted to DTOs
     */
    List<${entity.className}${cfg.dtoSuffix}> findAll();
}