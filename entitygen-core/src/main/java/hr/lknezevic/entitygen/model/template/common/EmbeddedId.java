package hr.lknezevic.entitygen.model.template.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents an embedded ID in an entity.
 * Contains the class name and a list of fields that make up the embedded ID.
 */
@Data
@Builder
public class EmbeddedId {
    private String className;
    private List<Field> fields;
}
