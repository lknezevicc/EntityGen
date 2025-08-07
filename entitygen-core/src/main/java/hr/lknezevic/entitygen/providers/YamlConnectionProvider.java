package hr.lknezevic.entitygen.providers;

import hr.lknezevic.entitygen.exceptions.unchecked.ConnectionProviderException;
import hr.lknezevic.entitygen.model.yaml.SpringYamlConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Provides a database connection using configuration loaded from a YAML file.
 */
public class YamlConnectionProvider extends AbstractConnectionProvider {
    private final SpringYamlConfig springYamlConfig;

    public YamlConnectionProvider(String yamlFile) {
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(SpringYamlConfig.class, options);
        Yaml yaml = new Yaml(constructor);

        try (InputStream is = Files.newInputStream(Path.of(yamlFile))) {
            springYamlConfig = yaml.load(is);
        } catch (IOException e) {
            throw new ConnectionProviderException("Failed to load properties from " + yamlFile, e);
        }
    }

    @Override
    protected String getUrl() {
        return springYamlConfig.getDataSourceConfig().getUrl();
    }

    @Override
    protected String getUser() {
        return springYamlConfig.getDataSourceConfig().getUser();
    }

    @Override
    protected String getPassword() {
        return springYamlConfig.getDataSourceConfig().getPassword();
    }

    @Override
    protected String getDriver() {
        return springYamlConfig.getDataSourceConfig().getDriverClassName();
    }
}
