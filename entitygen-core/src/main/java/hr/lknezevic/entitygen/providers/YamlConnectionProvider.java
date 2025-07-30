package hr.lknezevic.entitygen.providers;

import hr.lknezevic.entitygen.model.yaml.SpringConfig;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class YamlConnectionProvider extends AbstractConnectionProvider {
    private final SpringConfig springConfig;

    public YamlConnectionProvider(String yamlFile) {
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(SpringConfig.class, options);
        Yaml yaml = new Yaml(constructor);

        try (InputStream is = Files.newInputStream(Path.of(yamlFile))) {
            springConfig = yaml.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties from " + yamlFile, e);
        }
    }

    @Override
    protected String getUrl() {
        return springConfig.getDataSourceConfig().getUrl();
    }

    @Override
    protected String getUser() {
        return springConfig.getDataSourceConfig().getUser();
    }

    @Override
    protected String getPassword() {
        return springConfig.getDataSourceConfig().getPassword();
    }

    @Override
    protected String getDriver() {
        return springConfig.getDataSourceConfig().getDriverClassName();
    }
}
