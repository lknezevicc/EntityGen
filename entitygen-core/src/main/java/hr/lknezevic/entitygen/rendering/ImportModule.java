package hr.lknezevic.entitygen.rendering;

import hr.lknezevic.entitygen.template.TemplateConst;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Represents a module that constructs an import statement for a given import name.
 */
@Builder
@RequiredArgsConstructor
public class ImportModule implements ModuleBuilder {
    private final String importName;

    /**
     * Constructs an import statement based on the provided import name.
     *
     * @return a string representation of the import statement, or an empty string if the import name is empty
     */
    @Override
    public String construct() {
        if (importName.isEmpty()) return "";
        return String.format(TemplateConst.IMPORT_STATEMENT, importName);
    }
}
