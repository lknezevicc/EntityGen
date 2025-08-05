package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * REST Controller for {@link ${template.userConfig.entityPackage}.${template.entityName}} entity operations.
 * <p>
 * Provides RESTful endpoints for ${template.entity.tableName} entity management.
 * All responses use ResponseEntity for proper HTTP status handling.
 *
 * @author ${template.userConfig.javadocAuthor}
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/${template.entity.tableName?lower_case?replace("_", "-")}")
public class ${template.controllerName} {

    private final ${template.serviceName} service;

    ${template.modelBody}
}