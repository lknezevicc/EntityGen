package hr.lknezevic.entitygen.model;

import lombok.*;

import java.util.List;

/**
 * Represents a column in a database table.
 * This class encapsulates various properties of a column such as its name, type,
 * constraints, and other attributes.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Column {
    private String name;             // Ime stupca
    private Integer dataType;        // JDBC tip (java.sql.Types)
    private String typeName;         // Izvorni tip iz baze (npr. VARCHAR, INT, ENUM, ...)

    private boolean nullable;        // true ako dopušta null
    private boolean primaryKey;      // je li primarni ključ
    private boolean autoIncrement;   // autoinkrementiran?
    private boolean unique;          // jedinstven?

    private String defaultValue;     // default vrijednost
    private String comment;          // opis stupca (ako baza podržava)

    private Integer length;          // npr. VARCHAR(255)
    private Integer precision;       // DECIMAL(10,2)
    private Integer scale;           // DECIMAL(10,2)

    private List<String> enumValues; // ako je ENUM (MySQL / PostgreSQL)

    private boolean unsigned;        // unsigned broj?
    private boolean generated;       // virtualni/generirani stupac?
    private boolean isLob;           // true for TEXT, CLOB, LONGTEXT, etc.

    // 👇 OVO DODAJ — izračunato polje za generiranje entiteta
    private String javaType;         // npr. String, Integer, Boolean — za Javu
}
