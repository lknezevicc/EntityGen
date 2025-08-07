package hr.lknezevic.entitygen.analyzer.imports;

import java.util.List;

/**
 * Interface for analyzing and providing imports for various components in the application.
 */
public interface ImportAnalyzer {
    List<String> getImports();
}
