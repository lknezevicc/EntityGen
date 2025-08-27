package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.config.UserConfigProperties;
import hr.lknezevic.entitygen.exceptions.unchecked.ConfigurationLoadException;
import hr.lknezevic.entitygen.model.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

/**
 * Maven Mojo for generating entity classes based on the provided configuration.
 * This Mojo reads the configuration from a properties file and generates entities accordingly.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo {
    @Parameter(property = "springConfigPath",
            defaultValue = "${project.basedir}/src/main/resources/application.properties")
    private String springConfigPath;

    @Parameter(property = "basePackage", required = true)
    private String basePackage;

    @Parameter(property = "activeProfile", defaultValue = "default")
    private String activeProfile;

    /**
     * Executes the Mojo to generate entity classes based on the provided configuration.
     * It reads the configuration from the specified properties file and generates entities accordingly.
     *
     * @throws MojoExecutionException if an error occurs during execution
     */
    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("EntityGen running...");

        UserConfig userConfig = UserConfig.defaultConfig();
        try {
            userConfig = new UserConfigProperties(springConfigPath, basePackage).createUserConfig();
        } catch (ConfigurationLoadException e) {
            getLog().warn(String.format("Failed to load properties from %s, using default properties", springConfigPath), e);
        }

        MetadataRunner metadataRunner = new MetadataRunner(userConfig, springConfigPath, activeProfile);
        List<Schema> schemas = metadataRunner.generate();

        if (schemas.isEmpty()) {
            getLog().warn("No schemas found. Please check your configuration.");
            return;
        }

        EntityTemplateRunner entityTemplateRunner = new EntityTemplateRunner(schemas.getLast().getTables(), userConfig);
        entityTemplateRunner.generateComponents();

        getLog().info("EntityGen done.");
    }
}
