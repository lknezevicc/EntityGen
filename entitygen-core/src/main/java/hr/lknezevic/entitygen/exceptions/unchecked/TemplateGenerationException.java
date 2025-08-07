package hr.lknezevic.entitygen.exceptions.unchecked;

public class TemplateGenerationException extends RuntimeException {

    public TemplateGenerationException(String message) {
        super(message);
    }

    public TemplateGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateGenerationException(Throwable cause) {
        super(cause);
    }
}
