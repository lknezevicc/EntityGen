package ${package};

import org.springframework.data.jpa.repository.JpaRepository;
import ${entityPackage}.${entity.className};
<#if entity.hasCompositeKey && entity.embeddedId??>
import ${entity.embeddedId.package}.${entity.embeddedId.className};
</#if>

<#if entity.hasCompositeKey && entity.embeddedId??>
public interface ${entity.className}Repository extends JpaRepository<${entity.className}, ${entity.embeddedId.className}> {
}
<#else>
public interface ${entity.className}Repository extends JpaRepository<${entity.className}, ${entity.fields?filter(field -> field.primaryKey).first().javaType}> {
}
</#if>