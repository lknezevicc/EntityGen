package hr.lknezevic.entitygen.enums;

import lombok.Getter;

@Getter
public enum SpringProperties {
    URL("spring.datasource.url"),
    USER("spring.datasource.username"),
    PASSWORD("spring.datasource.password"),
    DRIVER("spring.datasource.driver-class-name");

    private final String propertyValue;

    SpringProperties(String propertyValue) {
        this.propertyValue = propertyValue;
    }
}
