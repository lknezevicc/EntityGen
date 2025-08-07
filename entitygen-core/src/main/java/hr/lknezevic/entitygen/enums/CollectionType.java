package hr.lknezevic.entitygen.enums;

import lombok.Getter;

/**
 * Enum representing different collection types.
 */
@Getter
public enum CollectionType {
    LIST("List"),
    SET("Set"),
    LINKED_HASH_SET("LinkedHashSet");

    private final String value;

    CollectionType(String value) {
        this.value = value;
    }

}
