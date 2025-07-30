package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.config.UserConfig;
import hr.lknezevic.entitygen.config.UserConfigProperties;
import hr.lknezevic.entitygen.model.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.util.List;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class GenerateMojo extends AbstractMojo {
    @Parameter(property = "springConfigPath",
            defaultValue = "${project.basedir}/src/main/resources/application.properties")
    private String springConfigPath;

    @Parameter(property = "basePackage", required = true)
    private String basePackage;

    @Parameter(property = "activeProfile", defaultValue = "default")
    private String activeProfile;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("EntityGen running...");

        UserConfig userConfig = UserConfig.builder().build();
        try {
            userConfig = new UserConfigProperties(springConfigPath, basePackage).createUserConfig();
        } catch (IOException e) {
            getLog().error("Failed to load properties from " + springConfigPath, e);
            getLog().info("Using default properties");
        }

        EntityGenRunner entityGenRunner = new EntityGenRunner(springConfigPath, activeProfile);
        List<Schema> schemas = entityGenRunner.generate();

        EntityTemplateRunner entityTemplateRunner = new EntityTemplateRunner(schemas.getLast().getTables(), userConfig);
        entityTemplateRunner.generateComponents();

        getLog().info("EntityGen done.");
    }
}
