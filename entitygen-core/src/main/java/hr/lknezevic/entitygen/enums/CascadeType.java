package hr.lknezevic.entitygen.enums;

import lombok.Getter;

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
