package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * Repository interface for {@link ${template.userConfig.entityPackage}.${template.entity.className}} entity.
 * <p>
 * Provides CRUD operations and custom queries for ${template.entity.tableName} entity.
 *
 * @author ${template.userConfig.javadocAuthor}
 */
@Repository
public interface ${template.modelBody} {
}