package ${template.componentPackage};

<#list template.imports as imp>
${imp}
</#list>

/**
 * Embeddable class for {@link ${template.userConfig.entityPackage}.${template.entity.className}} entity operations.
 * <p>
 * Embeddable class for composite primary key of ${template.entity.tableName} table.
 *
 * @author ${template.userConfig.javadocAuthor}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
@Generated(value="EntityGen Generator", date="${date}")
public class ${template.embeddedIdName} implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    ${template.modelBody}
}
