package ${cfg.controllerPackage};

import ${cfg.dtoPackage}.${entity.className}${cfg.dtoSuffix};
import ${cfg.servicePackage}.${entity.className}${cfg.serviceSuffix};

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for {@link ${cfg.entityPackage}.${entity.className}} entity operations.
 * <p>
 * Provides RESTful endpoints for ${entity.tableName} entity management.
 * All responses use ResponseEntity for proper HTTP status handling.
 *
 * @author ${cfg.javadocAuthor}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/${entity.tableName?lower_case}")
public class ${entity.className}${cfg.controllerSuffix} {

    private final ${entity.className}${cfg.serviceSuffix} service;

    /**
     * Retrieves all entities as DTOs.
     *
     * @return ResponseEntity with list of all DTOs and HTTP 200 status
     */
    @GetMapping
    public ResponseEntity<List<${entity.className}${cfg.dtoSuffix}>> findAll() {
        List<${entity.className}${cfg.dtoSuffix}> entities = service.findAll();
        return entities.isEmpty() 
            ? ResponseEntity.noContent().build() 
            : ResponseEntity.ok(entities);
    }
}