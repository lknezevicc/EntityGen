package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * JPA Entity for table ${template.entity.tableName}
 * <#if template.entity.schema??>Schema: ${template.entity.schema}</#if>
 * <#if template.entity.catalog??>Catalog: ${template.entity.catalog}</#if>
 *
 * @author ${template.userConfig.javadocAuthor}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "${template.entity.tableName}"<#if template.entity.schema??>,
    schema = "${template.entity.schema}"</#if><#if template.entity.uniqueConstraints?has_content>,
    uniqueConstraints = {
<#list template.entity.uniqueConstraints as uc>
        @UniqueConstraint(name = "${uc.name}", columnNames = {<#list uc.columns as col>"${col}"<#if col_has_next>, </#if></#list>})<#if uc_has_next>,</#if>
</#list>
    }</#if>
)
public class ${template.entityName} {

    ${template.modelBody}
}
