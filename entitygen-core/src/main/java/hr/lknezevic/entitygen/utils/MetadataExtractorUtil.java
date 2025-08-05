package hr.lknezevic.entitygen.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Utility class for database metadata extraction.
 *
 * @author leonknezevic
 */
public class MetadataExtractorUtil {

    /**
     * Resolves the Java type corresponding to an SQL data type.
     *
     * @param dataType the SQL data type
     * @param precision the precision of the data type, if applicable
     * @param scale the scale of the data type, if applicable
     * @return the corresponding Java type as a String
     */
    public static String resolveJavaType(int dataType, Integer precision, Integer scale) {
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
                if (precision != null && precision > 1) {
                    yield "byte[]";
                } else {
                    yield "Boolean";
                }
            }

            case Types.DATE -> "LocalDate";
            case Types.TIME -> "LocalTime";
            case Types.TIMESTAMP -> "LocalDateTime";

            default -> "Object";
        };
    }

    /**
     * Checks if the given ResultSet contains a column of type CLOB or NCLOB.
     *
     * @param rs the ResultSet to check
     * @param dataType the SQL data type of the column
     * @return true if the column is of type CLOB or NCLOB, false otherwise
     * @throws SQLException if an SQL error occurs
     */
    public static boolean isLobType(ResultSet rs, int dataType) throws SQLException {
        String typeName = rs.getString("TYPE_NAME");

        return dataType == Types.CLOB
                || dataType == Types.NCLOB
                || "CLOB".equalsIgnoreCase(typeName)
                || "NCLOB".equalsIgnoreCase(typeName);
    }
}
