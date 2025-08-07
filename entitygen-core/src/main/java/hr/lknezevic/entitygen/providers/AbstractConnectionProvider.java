package hr.lknezevic.entitygen.providers;

import hr.lknezevic.entitygen.exceptions.unchecked.ConnectionProviderException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract base class for providing database connections.
 * This class requires subclasses to implement methods to provide the database URL, user, password, and driver.
 */
public abstract class AbstractConnectionProvider implements ConnectionProvider {
    protected abstract String getUrl();
    protected abstract String getUser();
    protected abstract String getPassword();
    protected abstract String getDriver();

    /**
     * Returns a new database connection using the provided URL, user, password, and driver.
     *
     * @return a Connection object
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Connection getConnection() throws SQLException {
        String url = getUrl();
        String user = getUser();
        String password = getPassword();
        String driver = getDriver();

        if (url == null || driver == null) {
            throw new ConnectionProviderException("Missing datasource url or driver-class-name");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new ConnectionProviderException("JDBC Driver not found: " + driver, e);
        }

        return user != null && password != null ?
                DriverManager.getConnection(url, user, password) :
                DriverManager.getConnection(url);
    }
}
