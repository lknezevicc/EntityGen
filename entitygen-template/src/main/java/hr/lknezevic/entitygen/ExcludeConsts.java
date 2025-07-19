package hr.lknezevic.entitygen;

import java.util.List;

public class ExcludeConsts {
    public static final List<String> TYPES_WITHOUT_LENGTH = List.of(
            "Integer",
            "int",
            "Long",
            "long",
            "Short",
            "short",
            "Byte",
            "byte",
            "Boolean",
            "boolean",
            "BigDecimal",
            "Float",
            "float",
            "Double",
            "double",
            "Date",
            "LocalDate",
            "LocalDateTime",
            "OffsetDateTime",
            "ZonedDateTime",
            "Instant",
            "Timestamp",
            "Time"
    );


}
