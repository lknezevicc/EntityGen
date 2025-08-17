package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * Data Transfer Object for {@link ${template.userConfig.entityPackage}.${template.entity.className}} entity.
 * <p>
 * Contains all fields from the entity with relation IDs for efficient data transfer.
 * Relations are represented by their IDs to avoid circular references and lazy loading issues.
 *
 * @author ${template.userConfig.javadocAuthor}
 */
@Generated(value="EntityGen Generator", date="${date}")
public record ${template.dtoName}(
    ${template.modelBody}
) {}