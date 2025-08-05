package hr.lknezevic.entitygen.enums;

import lombok.Getter;

/**
 * Enum representing different types of cascade operations in JPA.
 *
 * @author leonknezevic
 */
@Getter
public enum CascadeType {
    ALL("CascadeType.ALL"),
    PERSIST("CascadeType.PERSIST"),
    MERGE("CascadeType.MERGE"),
    REMOVE("CascadeType.REMOVE"),
    REFRESH("CascadeType.REFRESH"),
    DETACH("CascadeType.DETACH"),
    NONE("CascadeType.NONE"),
    PERSIST_MERGE("{CascadeType.PERSIST, CascadeType.MERGE}");

    private final String value;

    CascadeType(String value) {
        this.value = value;
    }
}
