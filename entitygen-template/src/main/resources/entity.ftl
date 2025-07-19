package ${packageName};

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.*;
import java.time.*;
import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "${entity.tableName}"<#if entity.schema??>, 
    schema = "${entity.schema}"</#if>,
    <#if entity.hasCompositeKey>
    uniqueConstraints = {
        <#list entity.uniqueConstraints as uc>
        @UniqueConstraint(name = "${uc.name}", columnNames = {<#list uc.columns as col>"${col}"<#if col_has_next>, </#if></#list>})<#if uc_has_next>,</#if>
        </#list>
    }
    </#if>
)
public class ${entity.className} implements Serializable {

    <#-- EmbeddedId ako postoji -->
    <#if entity.hasCompositeKey && entity.embeddedId??>
    @EmbeddedId
    private ${entity.embeddedId.className} id;
    </#if>

    <#-- Ostala polja (ako nije embeddedId, ili ako su dodatna) -->
    <#list entity.fields as field>
    <#if !entity.hasCompositeKey || !field.primaryKey>
    @Column(name = "${field.columnName}"<#if field.length?has_content>, length = ${field.length}</#if><#if field.precision??>, precision = ${field.precision}</#if><#if field.scale??>, scale = ${field.scale}</#if><#if !field.nullable>, nullable = false</#if><#if field.unique>, unique = true</#if>)
    <#if field.autoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    private ${field.javaType} ${field.name};
    </#if>
    </#list>

    <#-- Relacije -->
    <#list entity.relations as rel>
    <#if rel.type == "MANY_TO_ONE">
    @ManyToOne
    <#if rel.optional?? && !rel.optional>
    @JoinColumn(name = "${rel.joinColumnName}", referencedColumnName = "${rel.referencedColumnName}", nullable = false)
    <#else>
    @JoinColumn(name = "${rel.joinColumnName}", referencedColumnName = "${rel.referencedColumnName}")
    </#if>
    private ${rel.targetEntityClass} ${rel.fieldName};
    </#if>

    <#if rel.type == "ONE_TO_MANY">
    @OneToMany(mappedBy = "${rel.mappedBy}"<#if rel.orphanRemoval>, orphanRemoval = true</#if><#if rel.cascadeType??>, cascade = CascadeType.${rel.cascadeType}</#if>)
    private ${rel.collectionType?default("List")}<${rel.targetEntityClass}> ${rel.fieldName};
    </#if>

    <#if rel.type == "MANY_TO_MANY">
    @ManyToMany
    @JoinTable(
        name = "${rel.joinTableName}",
        joinColumns = @JoinColumn(name = "${rel.joinColumnName}", referencedColumnName = "${rel.referencedColumnName}"),
        inverseJoinColumns = @JoinColumn(name = "${rel.inverseJoinColumnName}", referencedColumnName = "${rel.inverseReferencedColumnName}")
    )
    private ${rel.collectionType?default("Set")}<${rel.targetEntityClass}> ${rel.fieldName};
    </#if>

    <#if rel.type == "ONE_TO_ONE">
    @OneToOne
    @JoinColumn(name = "${rel.joinColumnName}", referencedColumnName = "${rel.referencedColumnName}")
    private ${rel.targetEntityClass} ${rel.fieldName};
    </#if>
    </#list>
}