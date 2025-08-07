package hr.lknezevic.entitygen.model.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for creating formatted strings based on a template and parameters.
 */
public class TemplateFactory {
    private final String template;
    private final List<Object> params = new ArrayList<>();

    private TemplateFactory(String template) {
        this.template = template;
    }

    public static TemplateFactoryBuilder builder() {
        return new TemplateFactoryBuilder();
    }

    /**
     * Formats the template with the provided parameters.
     *
     * @return a formatted string based on the template and parameters
     */
    public String format() {
        if (params.isEmpty()) {
            return template;
        }

        return String.format(template, params.toArray());
    }

    /**
     * Adds a single parameter to the list of parameters for formatting.
     *
     * @param param the parameter to add
     * @return the current instance of TemplateFactory for method chaining
     */
    public TemplateFactory addParam(Object param) {
        params.add(param);
        return this;
    }

    /**
     * Adds multiple parameters to the list of parameters for formatting.
     *
     * @param newParams the parameters to add
     * @return the current instance of TemplateFactory for method chaining
     */
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
