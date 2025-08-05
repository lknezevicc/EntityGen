package hr.lknezevic.entitygen.model.template.modules;

import hr.lknezevic.entitygen.model.template.TemplateConst;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Represents a module that constructs an import statement for a given import name.
 *
 * @author leonknezevic
 */
@Builder
@RequiredArgsConstructor
public class ImportModule implements ModuleBuilder {
    private final String importName;

    @Override
    public String construct() {
        if (importName.isEmpty()) return "";
        return String.format(TemplateConst.IMPORT_STATEMENT, importName);
    }
}
