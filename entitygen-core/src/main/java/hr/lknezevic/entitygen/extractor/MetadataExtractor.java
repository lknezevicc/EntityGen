package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.model.*;

import java.sql.*;
import java.util.List;

public interface MetadataExtractor {
    List<Schema> extractSchemas(Connection connection);
    List<Table> extractTables(DatabaseMetaData metaData, String schemaName);
    List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName);
    List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName);
    List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName);

    default String resolveJavaType(int dataType, Integer precision, Integer scale) {
        return switch (dataType) {
            case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.CLOB -> "String";

            case Types.INTEGER -> "Integer";
            case Types.BIGINT -> "Long";
            case Types.SMALLINT -> "Short";
            case Types.TINYINT -> "Byte";
            case Types.DOUBLE-> "Double";
            case Types.FLOAT, Types.REAL -> "Float";
            case Types.NUMERIC, Types.DECIMAL -> {
                if (precision != null && scale != null) {
                    if (scale == 0 && precision <= 9) yield "Integer";
                    if (scale == 0 && precision <= 18) yield "Long";
                }
                yield "BigDecimal";
            }

            case Types.BOOLEAN -> "Boolean";
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> "byte[]";
            case Types.BIT -> {
                if (precision != null && precision == 1) yield "Boolean";
                yield "byte[]";
            }

            case Types.DATE -> "LocalDate";
            case Types.TIME -> "LocalTime";
            case Types.TIMESTAMP -> "LocalDateTime";

            default -> "Object";
        };
    }

    default boolean isLobType(ResultSet rs, int dataType) throws SQLException {
        String typeName = rs.getString("TYPE_NAME");
        return dataType == Types.CLOB
                || dataType == Types.LONGVARCHAR
                || "TEXT".equalsIgnoreCase(typeName)
                || "LONGTEXT".equalsIgnoreCase(typeName)
                || "MEDIUMTEXT".equalsIgnoreCase(typeName)
                || "NTEXT".equalsIgnoreCase(typeName)
                || "NCLOB".equalsIgnoreCase(typeName);
    }

}
