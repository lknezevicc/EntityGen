package hr.lknezevic.entitygen.exceptions.unchecked;

public class MetadataExtractionException extends RuntimeException {

    public MetadataExtractionException(String message) {
        super(message);
    }

    public MetadataExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MetadataExtractionException(Throwable cause) {
        super(cause);
    }
}
