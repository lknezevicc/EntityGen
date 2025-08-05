package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * Service interface for {@link ${template.userConfig.entityPackage}.${template.entity.className}} business logic.
 * <p>
 * Provides high-level operations for ${template.entity.tableName} entity management.
 *
 * @author ${template.userConfig.javadocAuthor}
 */
public interface ${template.serviceName} {
    ${template.modelBody}
}