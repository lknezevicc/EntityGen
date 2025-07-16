package hr.lknezevic.entitygen.model.yaml;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DataSourceConfig {
    private String url;
    private String user;
    private String password;
    private String driverClassName;
}
