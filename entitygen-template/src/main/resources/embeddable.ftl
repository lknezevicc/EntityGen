package ${packageName};

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ${entity.embeddedId.className} implements Serializable {

    <#list entity.embeddedId.fields as field>
    @Column(name = "${field.columnName}"<#if field.length?has_content && !(noLength?seq_contains(field.javaType))>, length = ${field.length}</#if><#if !field.nullable>, nullable = false</#if>)
    private ${field.javaType} ${field.name};
    </#list>
}