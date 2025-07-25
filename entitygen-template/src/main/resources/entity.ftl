package ${packageName};

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * Entitet za tablicu ${entity.tableName}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "${entity.tableName}"<#if entity.schema??>,
    schema = "${entity.schema}"</#if><#if entity.uniqueConstraints?has_content>,
    uniqueConstraints = {
        <#list entity.uniqueConstraints as uc>
        @UniqueConstraint(name = "${uc.name}", columnNames = {<#list uc.columns as col>"${col}"<#if col_has_next>, </#if></#list>})<#if uc_has_next>,</#if>
        </#list>
    }</#if>
)
public class ${entity.className} implements Serializable {

<#-- KORAK 1: Skup stranih ključeva u relacijama -->
<#assign fkColumnNames = {} >
<#list entity.relations as rel>
    <#if rel.joinColumns?? && rel.joinColumns?size &gt; 0>
        <#list rel.joinColumns as jcName>
            <#assign fkColumnNames = fkColumnNames + { (jcName) : true } >
        </#list>
    </#if>
</#list>

<#-- KORAK 2: Primarni ključevi -->
<#if entity.hasCompositeKey && entity.embeddedId??>
    @EmbeddedId
    private ${entity.embeddedId.className} id;
</#if>

<#if !entity.hasCompositeKey>
    <#list entity.fields as field>
        <#if field.primaryKey>
    @Id
    <#if field.autoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${field.columnName}"<#if !field.nullable>, nullable = false</#if><#if field.unique>, unique = true</#if>)
    private ${field.javaType} ${field.name};
        </#if>
    </#list>
</#if>

<#-- KORAK 3: Ostala polja koja nisu primarni ključ niti strani ključ -->
<#list entity.fields as field>
    <#if !field.primaryKey && !fkColumnNames[field.columnName]?has_content>
    @Column(name = "${field.columnName}"<#if !field.nullable>, nullable = false</#if><#if field.unique>, unique = true</#if><#if field.length??>, length = ${field.length}</#if>)
    private ${field.javaType} ${field.name};
    </#if>
</#list>

<#-- RELACIJE -->
<#list entity.relations as rel>
    <#assign relType = rel.type?string>
    <#assign annotationParams = []>
    <#if rel.fetchType??>
        <#assign annotationParams += ["fetch = FetchType." + rel.fetchType?string]>
    </#if>
    <#if rel.cascadeType??>
        <#assign annotationParams += ["cascade = CascadeType." + rel.cascadeType?string]>
    </#if>
    <#if rel.mappedBy??>
        <#assign annotationParams += ["mappedBy = \"" + rel.mappedBy + "\""]>
    </#if>

    <#if relType == "MANY_TO_ONE">
    /**
     * MANY_TO_ONE veza na entitet ${rel.targetEntityClass}
     */
    @ManyToOne<#if annotationParams?size != 0>(${annotationParams?join(", ")})</#if>
    </#if>

    <#if relType == "ONE_TO_ONE">
    /**
     * ONE_TO_ONE veza na entitet ${rel.targetEntityClass}
     */
    @OneToOne<#if annotationParams?size != 0>(${annotationParams?join(", ")})</#if>
    </#if>

    <#if rel.mapsId??>
    @MapsId<#if rel.mapsId?has_content>("${rel.mapsId}")</#if>
    </#if>

    <#if rel.joinColumns?size == 1>
        <#assign jcName = rel.joinColumns[0]>
        <#assign refColName = rel.referencedColumns[0]>
    @JoinColumn(name = "${jcName}", referencedColumnName = "${refColName}"<#if rel.optional?is_boolean && !rel.optional>, nullable = false</#if>)
    <#elseif rel.joinColumns?size &gt; 1>
    @JoinColumns({
        <#list 0..(rel.joinColumns?size - 1) as i>
        @JoinColumn(name = "${rel.joinColumns[i]}", referencedColumnName = "${rel.referencedColumns[i]}")<#if i + 1 < rel.joinColumns?size>,</#if>
        </#list>
    })
    </#if>
    private ${rel.targetEntityClass} ${rel.fieldName};

    <#if relType == "ONE_TO_MANY">
    /**
     * ONE_TO_MANY veza na entitete ${rel.targetEntityClass}
     */
    <#assign params = []>
    <#if rel.mappedBy??>
        <#assign params += ["mappedBy = \"" + rel.mappedBy + "\""]>
    </#if>
    <#if rel.fetchType??>
        <#assign params += ["fetch = FetchType." + rel.fetchType?string]>
    </#if>
    <#if rel.cascadeType??>
        <#assign params += ["cascade = CascadeType." + rel.cascadeType?string]>
    </#if>
    <#if rel.orphanRemoval?is_boolean && rel.orphanRemoval>
        <#assign params += ["orphanRemoval = true"]>
    </#if>
    @OneToMany<#if params?size != 0>(${params?join(", ")})</#if>
    <#assign collectionType = (rel.collectionType?string)!"LIST">
    <#if collectionType == "SET">
    private Set<${rel.targetEntityClass}> ${rel.fieldName};
    <#else>
    private List<${rel.targetEntityClass}> ${rel.fieldName};
    </#if>

    <#if relType == "MANY_TO_MANY">
    /**
     * MANY_TO_MANY veza na entitete ${rel.targetEntityClass}
     */
    @ManyToMany<#if annotationParams?size != 0>(${annotationParams?join(", ")})</#if>
    @JoinTable(
        name = "${rel.joinTableName}",
        joinColumns = <#if rel.joinColumns?size == 1>
            @JoinColumn(name = "${rel.joinColumns[0]}", referencedColumnName = "${rel.referencedColumns[0]}")
        <#else>{
            <#list 0..(rel.joinColumns?size - 1) as i>
            @JoinColumn(name = "${rel.joinColumns[i]}", referencedColumnName = "${rel.referencedColumns[i]}")<#if i + 1 < rel.joinColumns?size>, </#if>
            </#list>
        }</#if>,
        inverseJoinColumns = <#if rel.inverseJoinColumns?size == 1>
            @JoinColumn(name = "${rel.inverseJoinColumns[0]}", referencedColumnName = "${rel.inverseReferencedColumns[0]}")
        <#else>{
            <#list 0..(rel.inverseJoinColumns?size - 1) as i>
            @JoinColumn(name = "${rel.inverseJoinColumns[i]}", referencedColumnName = "${rel.inverseReferencedColumns[i]}")<#if i + 1 < rel.inverseJoinColumns?size>, </#if>
            </#list>
        }</#if>
    )
    <#if collectionType == "SET">
    private Set<${rel.targetEntityClass}> ${rel.fieldName};
    <#else>
    private List<${rel.targetEntityClass}> ${rel.fieldName};
    </#if>
</#list>
}