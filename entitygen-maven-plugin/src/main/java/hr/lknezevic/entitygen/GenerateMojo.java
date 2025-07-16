package hr.lknezevic.entitygen;

import hr.lknezevic.entitygen.model.Schema;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

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

        EntityGenRunner entityGenRunner = new EntityGenRunner(springConfigPath, activeProfile);
        List<Schema> schemas = entityGenRunner.generate();
        schemas.forEach(schema -> {
            getLog().info("EntityGen Schema: " + schema);
        });

        getLog().info("EntityGen done.");
    }
}
