package ${cfg.servicePackage}.impl;

import ${cfg.servicePackage}.${entity.className}${cfg.serviceSuffix};
import ${cfg.dtoPackage}.${entity.className}${cfg.dtoSuffix};
import ${cfg.repositoryPackage}.${entity.className}${cfg.repositorySuffix};
import ${cfg.entityPackage}.${entity.className};

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Service implementation for {@link ${entity.className}} business logic.
 * <p>
 * Handles entity-to-DTO conversion and repository operations.
 *
 * @author EntityGen Maven Plugin
 */
@Service
@RequiredArgsConstructor
public class ${entity.className}${cfg.serviceSuffix}Impl implements ${entity.className}${cfg.serviceSuffix} {

    private final ${entity.className}${cfg.repositorySuffix} repository;

    @Override
    public List<${entity.className}${cfg.dtoSuffix}> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Converts entity to DTO.
     *
     * @param entity the entity to convert
     * @return converted DTO
     */
    private ${entity.className}${cfg.dtoSuffix} toDto(${entity.className} entity) {
        return new ${entity.className}${cfg.dtoSuffix}(
<#if entity.hasCompositeKey && entity.embeddedId??>
            entity.get${entity.embeddedId.className?cap_first}()<#if entity.fields?has_content || entity.relations?has_content>,</#if>
<#else>
    <#list entity.fields as field>
        <#if field.primaryKey>
            entity.get${field.name?cap_first}()<#if entity.fields?has_content || entity.relations?has_content>,</#if>
            <#break>
        </#if>
    </#list>
</#if>
<#list entity.fields as field>
    <#if !field.primaryKey>
            entity.get${field.name?cap_first}()<#if field_has_next || entity.relations?has_content>,</#if>
    </#if>
</#list>
<#if entity.relations?has_content>
<#list entity.relations as rel>
    <#assign relType = rel.type?string>
    <#assign collectionType = (rel.collectionType?string)!"LIST">
    <#assign targetEntity = entityByClassName[rel.targetEntityClass]!>
    <#assign targetIdFieldName = "id">
    <#if targetEntity??>
        <#if targetEntity.hasCompositeKey && targetEntity.embeddedId??>
            <#assign targetIdFieldName = targetEntity.embeddedId.className?uncap_first>
        <#else>
            <#list targetEntity.fields as field>
                <#if field.primaryKey>
                    <#assign targetIdFieldName = field.name>
                    <#break>
                </#if>
            </#list>
        </#if>
    </#if>
    <#if relType == "ONE_TO_ONE" || relType == "MANY_TO_ONE">
            entity.get${rel.fieldName?cap_first}() != null ? entity.get${rel.fieldName?cap_first}().get${targetIdFieldName?cap_first}() : null<#if rel_has_next>,</#if>
    <#else>
            entity.get${rel.fieldName?cap_first}() != null ? 
                entity.get${rel.fieldName?cap_first}().stream()
                    .map(item -> item.get${targetIdFieldName?cap_first}())
                    .<#if collectionType == "SET">collect(Collectors.toSet())<#else>collect(Collectors.toList())</#if> : 
                <#if collectionType == "SET">new HashSet<>()<#else>new ArrayList<>()</#if><#if rel_has_next>,</#if>
    </#if>
</#list>
</#if>
        );
    }
}