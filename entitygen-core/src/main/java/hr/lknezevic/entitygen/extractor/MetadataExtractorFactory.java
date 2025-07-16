package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.extractor.impl.H2MetadataExtractor;
import hr.lknezevic.entitygen.extractor.impl.MySqlMetadataExtractor;
import hr.lknezevic.entitygen.filter.SchemaFilter;
import hr.lknezevic.entitygen.filter.SchemaFilterImpl;

public class MetadataExtractorFactory {

    public MetadataExtractor getMetadataExtractor(String databaseProductName) {
        SchemaFilter schemaFilter = new SchemaFilterImpl(databaseProductName);

        return switch (databaseProductName.toLowerCase()) {
            case "mysql" -> new MySqlMetadataExtractor(schemaFilter);
            case "h2" -> new H2MetadataExtractor(schemaFilter);
            default -> throw new UnsupportedOperationException("Unsupported database: " + databaseProductName);
        };
    }
}
