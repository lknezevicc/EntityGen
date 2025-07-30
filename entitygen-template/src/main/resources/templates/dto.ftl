package ${cfg.dtoPackage};

import java.util.List;
import java.util.Set;

<#if entity.hasCompositeKey && entity.embeddedId??>
import ${cfg.entityPackage}.${entity.embeddedId.className};
</#if>

<#-- Import embedded IDs from related entities -->
<#if entity.relations?has_content>
<#list entity.relations as rel>
    <#assign targetEntity = entityByClassName[rel.targetEntityClass]!>
    <#if targetEntity?? && targetEntity.hasCompositeKey && targetEntity.embeddedId??>
import ${cfg.entityPackage}.${targetEntity.embeddedId.className};
    </#if>
</#list>
</#if>

/**
 * Data Transfer Object for {@link ${cfg.entityPackage}.${entity.className}} entity.
 * <p>
 * Contains all fields from the entity with relation IDs for efficient data transfer.
 * Relations are represented by their IDs to avoid circular references and lazy loading issues.
 * <p>
 * Generated from table: {@code ${entity.tableName}}
 *
 * @author EntityGen Maven Plugin
 */
public record ${entity.className}${cfg.dtoSuffix}(
<#if entity.hasCompositeKey && entity.embeddedId??>
    ${entity.embeddedId.className} id,
<#else>
    <#list entity.fields as field>
        <#if field.primaryKey>
    ${field.javaType} ${field.name},
            <#break>
        </#if>
    </#list>
</#if>

<#list entity.fields as field>
    <#if !field.primaryKey>
    ${field.javaType} ${field.name}<#if field_has_next || entity.relations?has_content>,</#if>
    </#if>
</#list>

<#if entity.relations?has_content>
<#list entity.relations as rel>
    <#assign collectionType = (rel.collectionType?string)!"LIST">
    <#assign relType = rel.type?string>
    <#-- Determine ID type based on target entity -->
    <#assign idType = "Long">
    <#-- Find target entity by class name -->
    <#assign targetEntity = entityByClassName[rel.targetEntityClass]!>
    <#if targetEntity??>
        <#if targetEntity.hasCompositeKey && targetEntity.embeddedId??>
            <#assign idType = targetEntity.embeddedId.className>
        <#else>
            <#-- Find primary key field -->
            <#list targetEntity.fields as field>
                <#if field.primaryKey>
                    <#assign idType = field.javaType>
                    <#break>
                </#if>
            </#list>
        </#if>
    </#if>
    <#if relType == "ONE_TO_ONE" || relType == "MANY_TO_ONE">
    ${idType} ${rel.fieldName}Id<#if rel_has_next>,</#if>
    <#else>
        <#if collectionType == "SET">
    Set<${idType}> ${rel.fieldName}Ids<#if rel_has_next>,</#if>
        <#else>
    List<${idType}> ${rel.fieldName}Ids<#if rel_has_next>,</#if>
        </#if>
    </#if>
</#list>
</#if>
) {}