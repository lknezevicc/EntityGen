package hr.lknezevic.entitygen.extractor;

import hr.lknezevic.entitygen.model.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import java.util.List;

public interface MetadataExtractor {
    List<Schema> extractSchemas(Connection connection);
    List<Table> extractTables(DatabaseMetaData metaData, String schemaName);
    List<Column> extractColumns(DatabaseMetaData metaData, String schemaName, String tableName);
    List<ForeignKey> extractForeignKeys(DatabaseMetaData metaData, String schemaName, String tableName);
    List<UniqueConstraint> extractUniqueConstraints(DatabaseMetaData metaData, String schemaName, String tableName);

    default String resolveJavaType(int dataType) {
        return switch (dataType) {
            case Types.VARCHAR, Types.CHAR, Types.LONGVARCHAR, Types.CLOB -> "String";
            case Types.INTEGER -> "Integer";
            case Types.BIGINT -> "Long";
            case Types.SMALLINT -> "Short";
            case Types.TINYINT -> "Byte";
            case Types.BOOLEAN, Types.BIT -> "Boolean";
            case Types.DOUBLE, Types.FLOAT -> "Double";
            case Types.REAL -> "Float";
            case Types.NUMERIC, Types.DECIMAL -> "BigDecimal";
            case Types.DATE -> "LocalDate";
            case Types.TIME -> "LocalTime";
            case Types.TIMESTAMP -> "LocalDateTime";
            case Types.BINARY, Types.VARBINARY, Types.LONGVARBINARY, Types.BLOB -> "byte[]";
            default -> "Object";
        };
    }

    default String resolveAction(short rule) {
        return switch (rule) {
            case DatabaseMetaData.importedKeyCascade -> "CASCADE";
            case DatabaseMetaData.importedKeyRestrict -> "RESTRICT";
            case DatabaseMetaData.importedKeySetNull -> "SET_NULL";
            case DatabaseMetaData.importedKeyNoAction -> "NO_ACTION";
            case DatabaseMetaData.importedKeySetDefault -> "SET_DEFAULT";
            default -> "UNKNOWN";
        };
    }
}
