package hr.lknezevic.entitygen.providers;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for providing database connections.
 */
public interface ConnectionProvider {
    Connection getConnection() throws SQLException;
}
