package hr.lknezevic.entitygen.exceptions.unchecked;

public class ConnectionProviderException extends RuntimeException {

    public ConnectionProviderException(String message) {
        super(message);
    }

    public ConnectionProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionProviderException(Throwable cause) {
        super(cause);
    }
}
