package ${packageName};

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Embeddable klasa za složeni primarni ključ tablice ${entity.tableName}
 * 
 * Generirano automatski pomoću EntityGen Maven Plugin-a
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ${entity.embeddedId.className} implements Serializable {

    private static final long serialVersionUID = 1L;

    <#list entity.embeddedId.fields as field>
    /**
     * Dio primarnog ključa: ${field.columnName}<#if field.comment??> - ${field.comment}</#if>
     */
    @Column(name = "${field.columnName}"<#if field.length?has_content && !(noLength?seq_contains(field.javaType))>, length = ${field.length}</#if><#if !field.nullable>, nullable = false</#if><#if field.precision??>, precision = ${field.precision}</#if><#if field.scale??>, scale = ${field.scale}</#if>)
    private ${field.javaType} ${field.name};
    </#list>

    @Override
    public String toString() {
        return "${entity.embeddedId.className}{" +
               <#list entity.embeddedId.fields as field>
               "${field.name}=" + ${field.name}<#if field_has_next> + ", " + </#if>
               </#list>
               '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ${entity.embeddedId.className} other = (${entity.embeddedId.className}) obj;
        return <#list entity.embeddedId.fields as field>Objects.equals(${field.name}, other.${field.name})<#if field_has_next> &&
               </#if></#list>;
    }

    @Override
    public int hashCode() {
        return Objects.hash(<#list entity.embeddedId.fields as field>${field.name}<#if field_has_next>, </#if></#list>);
    }
}