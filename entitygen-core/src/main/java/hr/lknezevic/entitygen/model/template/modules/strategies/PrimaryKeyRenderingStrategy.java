package hr.lknezevic.entitygen.model.template.modules.strategies;

import hr.lknezevic.entitygen.model.template.common.Entity;

public interface PrimaryKeyRenderingStrategy {
    String render(Entity entity);
}
