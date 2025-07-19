package hr.lknezevic.entitygen.enums;

import lombok.Getter;

@Getter
public enum CollectionType {
    LIST("List"),
    SET("Set"),
    SORTED_SET("SortedSet"),
    LINKED_HASH_SET("LinkedHashSet"),
    MAP("Map"),
    COLLECTION("Collection"),;

    private final String value;

    CollectionType(String value) {
        this.value = value;
    }

}
