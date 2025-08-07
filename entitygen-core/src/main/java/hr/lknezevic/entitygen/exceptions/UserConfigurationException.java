package hr.lknezevic.entitygen.exceptions;

public class UserConfigurationException extends Exception {

    public UserConfigurationException(String message) {
        super(message);
    }

    public UserConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserConfigurationException(Throwable cause) {
        super(cause);
    }
}
