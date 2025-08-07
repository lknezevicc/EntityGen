package hr.lknezevic.entitygen.providers;

import hr.lknezevic.entitygen.enums.SpringProperties;
import hr.lknezevic.entitygen.exceptions.unchecked.ConnectionProviderException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Provides a database connection using properties loaded from a specified file.
 */
public class PropertiesConnectionProvider extends AbstractConnectionProvider {
    private final Properties properties;

    public PropertiesConnectionProvider(String propertiesFile) {
        properties = new Properties();
        try (InputStream is = Files.newInputStream(Path.of(propertiesFile))) {
            properties.load(is);
        } catch (IOException e) {
            throw new ConnectionProviderException("Failed to load properties from " + propertiesFile, e);
        }
    }

    @Override
    protected String getUrl() {
        return properties.getProperty(SpringProperties.URL.getValue());
    }

    @Override
    protected String getUser() {
        return properties.getProperty(SpringProperties.USER.getValue());
    }

    @Override
    protected String getPassword() {
        return properties.getProperty(SpringProperties.PASSWORD.getValue());
    }

    @Override
    protected String getDriver() {
        return properties.getProperty(SpringProperties.DRIVER.getValue());
    }
}
