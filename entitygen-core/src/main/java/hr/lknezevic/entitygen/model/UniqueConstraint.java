package hr.lknezevic.entitygen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a unique constraint in a database table.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniqueConstraint {
    private String name;
    private List<String> columns;
}
