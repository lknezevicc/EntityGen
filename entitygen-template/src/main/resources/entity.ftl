package ${packageName};

import jakarta.persistence.*;
import lombok.*;
<#if entity.relations?has_content>
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
</#if>

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;

/**
 * JPA Entity za tablicu ${entity.tableName}
 * <#if entity.schema??>Schema: ${entity.schema}</#if>
 * 
 * Generirano automatski pomoću EntityGen Maven Plugin-a
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private static final long serialVersionUID = 1L;

<#-- =============================================== -->
<#-- KORAK 1: Analiza Foreign Key kolona iz relacija -->
<#-- =============================================== -->
<#assign fkColumnNames = {} >
<#list entity.relations as rel>
    <#if rel.joinColumns?? && rel.joinColumns?size &gt; 0>
        <#list rel.joinColumns as jcName>
            <#assign fkColumnNames = fkColumnNames + { (jcName) : true } >
        </#list>
    </#if>
</#list>

<#-- ============================================ -->
<#-- KORAK 2: Primarni ključevi (ID polja) -->
<#-- ============================================ -->
<#if entity.hasCompositeKey && entity.embeddedId??>
    /**
     * Složeni primarni ključ
     */
    @EmbeddedId
    @Builder.Default
    private ${entity.embeddedId.className} id = new ${entity.embeddedId.className}();
</#if>

<#if !entity.hasCompositeKey>
    <#list entity.fields as field>
        <#if field.primaryKey>
    /**
     * Primarni ključ: ${field.columnName}
     */
    @Id
    <#if field.autoIncrement>
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${field.columnName}"<#if !field.nullable>, nullable = false</#if><#if field.unique>, unique = true</#if>)
    private ${field.javaType} ${field.name};
        </#if>
    </#list>
</#if>

<#-- ========================================== -->
<#-- KORAK 3: Ostala polja (ne-PK, ne-FK) -->
<#-- ========================================== -->
<#list entity.fields as field>
    <#if !field.primaryKey && !fkColumnNames[field.columnName]?has_content>
    /**
     * Polje: ${field.columnName}<#if field.comment??> - ${field.comment}</#if>
     */
    @Column(name = "${field.columnName}"<#if !field.nullable>, nullable = false</#if><#if field.unique>, unique = true</#if><#if field.length??>, length = ${field.length}</#if><#if field.precision??>, precision = ${field.precision}</#if><#if field.scale??>, scale = ${field.scale}</#if>)
    <#if field.defaultValue??>
    @Builder.Default
    private ${field.javaType} ${field.name} = <#if field.javaType == "String">"${field.defaultValue}"<#elseif field.javaType == "Boolean">${field.defaultValue?lower_case}<#else>${field.defaultValue}</#if>;
    <#else>
    private ${field.javaType} ${field.name};
    </#if>
    </#if>
</#list>

<#-- ========================================== -->
<#-- KORAK 4: JPA RELACIJE -->
<#-- ========================================== -->
<#list entity.relations as rel>
    <#assign relType = rel.type?string>
    
    <#-- MANY_TO_ONE relacije -->
    <#if relType == "MANY_TO_ONE">
    /**
     * ${relType} relacija prema ${rel.targetEntityClass}
     * <#if rel.joinColumns?has_content>FK: ${rel.joinColumns?join(", ")}</#if>
     */
    @ManyToOne<#assign params = []><#if rel.fetchType??><#assign params += ["fetch = FetchType." + rel.fetchType?string]></#if><#if rel.cascadeType??><#assign params += ["cascade = CascadeType." + rel.cascadeType?string]></#if><#if rel.optional?? && rel.optional?is_boolean><#assign params += ["optional = " + rel.optional?c]></#if><#if params?size != 0>(${params?join(", ")})</#if>
    <#if rel.joinColumns?size == 1>
    @JoinColumn(name = "${rel.joinColumns[0]}", referencedColumnName = "${rel.referencedColumns[0]}"<#if rel.optional?? && rel.optional?is_boolean && !rel.optional>, nullable = false</#if>)
    <#elseif rel.joinColumns?size &gt; 1>
    @JoinColumns({
        <#list 0..(rel.joinColumns?size - 1) as i>
        @JoinColumn(name = "${rel.joinColumns[i]}", referencedColumnName = "${rel.referencedColumns[i]}")<#if i + 1 < rel.joinColumns?size>,</#if>
        </#list>
    })
    </#if>
    private ${rel.targetEntityClass} ${rel.fieldName};
    </#if>

    <#-- ONE_TO_ONE relacije -->
    <#if relType == "ONE_TO_ONE">
    /**
     * ${relType} relacija prema ${rel.targetEntityClass}
     * <#if rel.mappedBy??>Mapped by: ${rel.mappedBy}<#else>Owning side</#if>
     */
    @OneToOne<#assign params = []><#if rel.fetchType??><#assign params += ["fetch = FetchType." + rel.fetchType?string]></#if><#if rel.cascadeType??><#assign params += ["cascade = CascadeType." + rel.cascadeType?string]></#if><#if rel.mappedBy??><#assign params += ["mappedBy = \"" + rel.mappedBy + "\""]></#if><#if rel.optional?? && rel.optional?is_boolean && !rel.mappedBy??><#assign params += ["optional = " + rel.optional?c]></#if><#if rel.orphanRemoval?? && rel.orphanRemoval?is_boolean && rel.orphanRemoval><#assign params += ["orphanRemoval = true"]></#if><#if params?size != 0>(${params?join(", ")})</#if>
    <#if rel.mapsId??>
    @MapsId<#if rel.mapsId?has_content>("${rel.mapsId}")</#if>
    </#if>
    <#if !rel.mappedBy?? && rel.joinColumns?size == 1>
    @JoinColumn(name = "${rel.joinColumns[0]}", referencedColumnName = "${rel.referencedColumns[0]}"<#if rel.optional?? && rel.optional?is_boolean && !rel.optional>, nullable = false</#if>)
    <#elseif !rel.mappedBy?? && rel.joinColumns?size &gt; 1>
    @JoinColumns({
        <#list 0..(rel.joinColumns?size - 1) as i>
        @JoinColumn(name = "${rel.joinColumns[i]}", referencedColumnName = "${rel.referencedColumns[i]}")<#if i + 1 < rel.joinColumns?size>,</#if>
        </#list>
    })
    </#if>
    private ${rel.targetEntityClass} ${rel.fieldName};
    </#if>

    <#-- ONE_TO_MANY relacije -->
    <#if relType == "ONE_TO_MANY">
    /**
     * ${relType} relacija prema ${rel.targetEntityClass}
     * Mapped by: ${rel.mappedBy!"N/A"}
     */
    @OneToMany<#assign params = []><#if rel.mappedBy??><#assign params += ["mappedBy = \"" + rel.mappedBy + "\""]></#if><#if rel.fetchType??><#assign params += ["fetch = FetchType." + rel.fetchType?string]></#if><#if rel.cascadeType??><#assign params += ["cascade = CascadeType." + rel.cascadeType?string]></#if><#if rel.orphanRemoval?? && rel.orphanRemoval?is_boolean && rel.orphanRemoval><#assign params += ["orphanRemoval = true"]></#if><#if params?size != 0>(${params?join(", ")})</#if>
    <#assign collectionType = (rel.collectionType?string)!"LIST">
    <#if collectionType == "SET">
    @Builder.Default
    private Set<${rel.targetEntityClass}> ${rel.fieldName} = new LinkedHashSet<>();
    <#else>
    @Builder.Default
    private List<${rel.targetEntityClass}> ${rel.fieldName} = new ArrayList<>();
    </#if>
    </#if>

    <#-- MANY_TO_MANY relacije -->
    <#if relType == "MANY_TO_MANY">
    /**
     * ${relType} relacija prema ${rel.targetEntityClass}
     * <#if rel.joinTableName??>Join Table: ${rel.joinTableName}</#if>
     * <#if rel.mappedBy??>Mapped by: ${rel.mappedBy}<#else>Owning side</#if>
     */
    @ManyToMany<#assign params = []><#if rel.fetchType??><#assign params += ["fetch = FetchType." + rel.fetchType?string]></#if><#if rel.cascadeType??><#assign params += ["cascade = CascadeType." + rel.cascadeType?string]></#if><#if rel.mappedBy??><#assign params += ["mappedBy = \"" + rel.mappedBy + "\""]></#if><#if params?size != 0>(${params?join(", ")})</#if>
    <#if !rel.mappedBy?? && rel.joinTableName??>
    @JoinTable(
        name = "${rel.joinTableName}",
        joinColumns = <#if rel.joinColumns?size == 1>@JoinColumn(name = "${rel.joinColumns[0]}", referencedColumnName = "${rel.referencedColumns[0]}")<#else>{
            <#list 0..(rel.joinColumns?size - 1) as i>
            @JoinColumn(name = "${rel.joinColumns[i]}", referencedColumnName = "${rel.referencedColumns[i]}")<#if i + 1 < rel.joinColumns?size>, </#if>
            </#list>
        }</#if>,
        inverseJoinColumns = <#if rel.inverseJoinColumns?size == 1>@JoinColumn(name = "${rel.inverseJoinColumns[0]}", referencedColumnName = "${rel.inverseReferencedColumns[0]}")<#else>{
            <#list 0..(rel.inverseJoinColumns?size - 1) as i>
            @JoinColumn(name = "${rel.inverseJoinColumns[i]}", referencedColumnName = "${rel.inverseReferencedColumns[i]}")<#if i + 1 < rel.inverseJoinColumns?size>, </#if>
            </#list>
        }</#if>
    )
    </#if>
    <#assign collectionType = (rel.collectionType?string)!"LIST">
    <#if collectionType == "SET">
    @Builder.Default
    private Set<${rel.targetEntityClass}> ${rel.fieldName} = new LinkedHashSet<>();
    <#else>
    @Builder.Default
    private List<${rel.targetEntityClass}> ${rel.fieldName} = new ArrayList<>();
    </#if>
    </#if>

</#list>

<#-- ========================================== -->
<#-- KORAK 5: Helper metode za relacije -->
<#-- ========================================== -->
<#assign hasOneToManyOrManyToMany = false>
<#list entity.relations as rel>
    <#if rel.type?string == "ONE_TO_MANY" || rel.type?string == "MANY_TO_MANY">
        <#assign hasOneToManyOrManyToMany = true>
        <#break>
    </#if>
</#list>

<#if hasOneToManyOrManyToMany>
    // ========================================
    // Helper metode za upravljanje relacijama
    // ========================================
    
    <#list entity.relations as rel>
        <#if rel.type?string == "ONE_TO_MANY">
            <#assign singularName = rel.fieldName?replace("List", "")?replace("Set", "")>
    /**
     * Dodaj ${rel.targetEntityClass} u ${rel.fieldName} kolekciju
     */
    public void add${rel.targetEntityClass}(${rel.targetEntityClass} ${singularName}) {
        if (this.${rel.fieldName} == null) {
            <#assign collectionType = (rel.collectionType?string)!"LIST">
            <#if collectionType == "SET">
            this.${rel.fieldName} = new LinkedHashSet<>();
            <#else>
            this.${rel.fieldName} = new ArrayList<>();
            </#if>
        }
        this.${rel.fieldName}.add(${singularName});
        <#if rel.mappedBy??>
        ${singularName}.set${rel.mappedBy?cap_first}(this);
        </#if>
    }

    /**
     * Ukloni ${rel.targetEntityClass} iz ${rel.fieldName} kolekcije
     */
    public void remove${rel.targetEntityClass}(${rel.targetEntityClass} ${singularName}) {
        if (this.${rel.fieldName} != null) {
            this.${rel.fieldName}.remove(${singularName});
            <#if rel.mappedBy??>
            ${singularName}.set${rel.mappedBy?cap_first}(null);
            </#if>
        }
    }
        </#if>
        
        <#if rel.type?string == "MANY_TO_MANY">
            <#assign singularName = rel.fieldName?replace("List", "")?replace("Set", "")>
    /**
     * Dodaj ${rel.targetEntityClass} u ${rel.fieldName} kolekciju
     */
    public void add${rel.targetEntityClass}(${rel.targetEntityClass} ${singularName}) {
        if (this.${rel.fieldName} == null) {
            <#assign collectionType = (rel.collectionType?string)!"LIST">
            <#if collectionType == "SET">
            this.${rel.fieldName} = new LinkedHashSet<>();
            <#else>
            this.${rel.fieldName} = new ArrayList<>();
            </#if>
        }
        this.${rel.fieldName}.add(${singularName});
    }

    /**
     * Ukloni ${rel.targetEntityClass} iz ${rel.fieldName} kolekcije
     */
    public void remove${rel.targetEntityClass}(${rel.targetEntityClass} ${singularName}) {
        if (this.${rel.fieldName} != null) {
            this.${rel.fieldName}.remove(${singularName});
        }
    }
        </#if>
    </#list>
</#if>

<#-- ========================================== -->
<#-- KORAK 6: toString, equals, hashCode override -->
<#-- ========================================== -->
    
    @Override
    public String toString() {
        return "${entity.className}{" +
               <#list entity.fields as field>
                   <#if field.primaryKey>
               "${field.name}=" + ${field.name} +
                   </#if>
               </#list>
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ${entity.className} other = (${entity.className}) obj;
        <#if entity.hasCompositeKey && entity.embeddedId??>
        return java.util.Objects.equals(id, other.id);
        <#else>
            <#list entity.fields as field>
                <#if field.primaryKey>
        return java.util.Objects.equals(${field.name}, other.${field.name});
                </#if>
            </#list>
        </#if>
    }

    @Override
    public int hashCode() {
        <#if entity.hasCompositeKey && entity.embeddedId??>
        return java.util.Objects.hash(id);
        <#else>
            <#list entity.fields as field>
                <#if field.primaryKey>
        return java.util.Objects.hash(${field.name});
                </#if>
            </#list>
        </#if>
    }
}