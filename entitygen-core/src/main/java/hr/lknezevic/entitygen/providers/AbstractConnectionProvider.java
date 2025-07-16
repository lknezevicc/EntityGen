package hr.lknezevic.entitygen.providers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class AbstractConnectionProvider implements ConnectionProvider {
    protected abstract String getUrl();
    protected abstract String getUser();
    protected abstract String getPassword();
    protected abstract String getDriver();

    @Override
    public Connection getConnection() throws SQLException {
        String url = getUrl();
        String user = getUser();
        String password = getPassword();
        String driver = getDriver();

        if (url == null || driver == null) {
            throw new IllegalArgumentException("Missing datasource url or driver-class-name");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver not found: " + driver, e);
        }

        return user != null && password != null ?
                DriverManager.getConnection(url, user, password) :
                DriverManager.getConnection(url);
    }
}
