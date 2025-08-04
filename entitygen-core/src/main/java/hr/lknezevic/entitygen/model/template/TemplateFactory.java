package hr.lknezevic.entitygen.model.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TemplateFactory {
    private final String template;
    private final List<Object> params = new ArrayList<>();

    private TemplateFactory(String template) {
        this.template = template;
    }

    public static TemplateFactoryBuilder builder() {
        return new TemplateFactoryBuilder();
    }

    public String format() {
        if (params.isEmpty()) {
            return template;
        }

        return String.format(template, params.toArray());
    }

    public TemplateFactory addParam(Object param) {
        params.add(param);
        return this;
    }

    public TemplateFactory addParams(Object... newParams) {
        Collections.addAll(params, newParams);
        return this;
    }

    public static class TemplateFactoryBuilder {
        private String template;

        public TemplateFactoryBuilder template(String template) {
            this.template = template;
            return this;
        }

        public TemplateFactory build() {
            return new TemplateFactory(template);
        }
    }
}
