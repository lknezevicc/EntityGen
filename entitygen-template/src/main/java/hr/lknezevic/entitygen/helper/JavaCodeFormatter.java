package hr.lknezevic.entitygen.helper;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

import java.util.HashMap;
import java.util.Map;

/**
 * Class for formatting Java code using Eclipse's CodeFormatter.
 *
 * @author leonknezevic
 */
public class JavaCodeFormatter {
    private final CodeFormatter formatter;

    public JavaCodeFormatter() {
        this.formatter = createFormatter();
    }

    private CodeFormatter createFormatter() {
        Map<String, String> options = new HashMap<>();

        // Basic formatting settings
        options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_CHAR, JavaCore.SPACE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
        options.put(DefaultCodeFormatterConstants.FORMATTER_INDENTATION_SIZE, "4");
        options.put(DefaultCodeFormatterConstants.FORMATTER_LINE_SPLIT, "120");

        // Inline braces - all set to END_OF_LINE for { on the same line
        options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_TYPE_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_METHOD_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_CONSTRUCTOR_DECLARATION, DefaultCodeFormatterConstants.END_OF_LINE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_BLOCK, DefaultCodeFormatterConstants.END_OF_LINE);
        options.put(DefaultCodeFormatterConstants.FORMATTER_BRACE_POSITION_FOR_SWITCH, DefaultCodeFormatterConstants.END_OF_LINE);

        // Method parameter alignment
        options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_PARAMETERS_IN_METHOD_DECLARATION,
                String.valueOf(DefaultCodeFormatterConstants.WRAP_COMPACT_FIRST_BREAK));

        // Method call argument alignment
        options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_ARGUMENTS_IN_METHOD_INVOCATION,
                String.valueOf(DefaultCodeFormatterConstants.WRAP_COMPACT));

        // Record component alignment (for newer Eclipse versions)
        options.put(DefaultCodeFormatterConstants.FORMATTER_ALIGNMENT_FOR_RECORD_COMPONENTS,
                String.valueOf(DefaultCodeFormatterConstants.WRAP_COMPACT));

        // Import organization
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_AFTER_IMPORTS, "1");
        options.put(DefaultCodeFormatterConstants.FORMATTER_BLANK_LINES_BEFORE_IMPORTS, "1");

        // Annotation formatting
        options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_ANNOTATION_ON_TYPE, JavaCore.INSERT);
        options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_ANNOTATION_ON_FIELD, JavaCore.INSERT);
        options.put(DefaultCodeFormatterConstants.FORMATTER_INSERT_NEW_LINE_AFTER_ANNOTATION_ON_METHOD, JavaCore.INSERT);

        return ToolFactory.createCodeFormatter(options);
    }

    public String formatJavaCode(String sourceCode) {
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            return sourceCode;
        }

        try {
            TextEdit edit = formatter.format(
                    CodeFormatter.K_COMPILATION_UNIT,
                    sourceCode,
                    0,
                    sourceCode.length(),
                    0,
                    System.lineSeparator()
            );

            if (edit != null) {
                Document document = new Document(sourceCode);
                edit.apply(document);
                return document.get();
            }

        } catch (MalformedTreeException | BadLocationException e) {
            System.err.println("Eclipse formatter failed: " + e.getMessage());
        }

        return basicCleanup(sourceCode);
    }

    private String basicCleanup(String code) {
        return code
                .replaceAll("\\n{3,}", "\n\n")
                .replaceAll("\\s+\\n", "\n")
                .replaceAll("\\{\\s*\\n\\s*\\n", "{\n")
                .replaceAll("\\n\\s*\\n\\s*\\}", "\n}");
    }

    public String formatComponent(String sourceCode) {
        if (sourceCode == null || sourceCode.trim().isEmpty()) {
            return sourceCode;
        }

        return sourceCode.replaceAll("(?m)^(\\s*private\\s+.*?;)$", "$1\n");
    }
}
