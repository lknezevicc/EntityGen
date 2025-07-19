package hr.lknezevic.entitygen.model.template;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmbeddedId {
    private String className;
    private List<Field> fields;
}
