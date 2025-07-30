package ${cfg.repositoryPackage};

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ${cfg.entityPackage}.${entity.className};
<#if entity.hasCompositeKey && entity.embeddedId??>
import ${cfg.entityPackage}.${entity.embeddedId.className};
</#if>

@Repository
<#if entity.hasCompositeKey && entity.embeddedId??>
public interface ${entity.className}${cfg.repositorySuffix} extends JpaRepository<${entity.className}, ${entity.embeddedId.className}> {
}
<#else>
<#-- Find the primary key field -->
<#assign primaryKeyField = "Long">
<#list entity.fields as field>
    <#if field.primaryKey>
        <#assign primaryKeyField = field.javaType>
        <#break>
    </#if>
</#list>
public interface ${entity.className}${cfg.repositorySuffix} extends JpaRepository<${entity.className}, ${primaryKeyField}> {
}
</#if>