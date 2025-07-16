package hr.lknezevic.entitygen.providers;

import hr.lknezevic.entitygen.enums.SpringProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesConnectionProvider extends AbstractConnectionProvider {
    private final Properties properties;

    public PropertiesConnectionProvider(String propertiesFile) {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + propertiesFile, e);
        }
    }

    @Override
    protected String getUrl() {
        return properties.getProperty(SpringProperties.URL.getPropertyValue());
    }

    @Override
    protected String getUser() {
        return properties.getProperty(SpringProperties.USER.getPropertyValue());
    }

    @Override
    protected String getPassword() {
        return properties.getProperty(SpringProperties.PASSWORD.getPropertyValue());
    }

    @Override
    protected String getDriver() {
        return properties.getProperty(SpringProperties.DRIVER.getPropertyValue());
    }
}
