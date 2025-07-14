package hr.lknezevic.entitygen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
        getLog().info("EntityGen Running...");

        if (activeProfile.equals("default")) {
            getLog().warn("Using 'default' profile.");
        } else {
            getLog().info("Active profile: " + activeProfile);
            springConfigPath = springConfigPath
                    .replace("application.properties", "application-" + activeProfile + ".properties");
        }

        try (FileInputStream in = new FileInputStream(springConfigPath)) {
            Properties props = new Properties();
            props.load(in);

            String url = props.getProperty("spring.datasource.url");
            String user = props.getProperty("spring.datasource.username");
            String pass = props.getProperty("spring.datasource.password");
            String driver = props.getProperty("spring.datasource.driver-class-name");

            if (url == null || user == null || pass == null) {
                throw new MojoExecutionException("Missing data!");
            }

            Class.forName(driver);

            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                getLog().info("Connected to database: " + conn.getMetaData().getURL());
                ResultSet rs = conn.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
                getLog().info("Tables:");
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    getLog().info(" - " + tableName);
                }


            } catch (SQLException e) {
                throw new MojoExecutionException("Cannot connect to database!", e);
            }

        } catch (IOException | ClassNotFoundException e) {
            throw new MojoExecutionException("Cannot read configuration.", e);
        }

        getLog().info("EntityGen done.");
    }
}
