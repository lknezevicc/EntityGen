package ${cfg.entityPackage};

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Embeddable klasa za složeni primarni ključ tablice ${entity.tableName}
 * 
 * @author ${cfg.javadocAuthor}
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
    <#-- Define types that should not have length attribute -->
    <#assign noLengthTypes = ["Integer", "Long", "BigDecimal", "Boolean", "LocalDate", "LocalDateTime", "LocalTime", "OffsetDateTime", "byte[]"]>
    @Column(name = "${field.columnName}"<#if field.length?has_content && !(noLengthTypes?seq_contains(field.javaType))>, length = ${field.length}</#if><#if !field.nullable>, nullable = false</#if><#if field.precision??>, precision = ${field.precision}</#if><#if field.scale??>, scale = ${field.scale}</#if>)
    private ${field.javaType} ${field.name};
    </#list>
    
}
