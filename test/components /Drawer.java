package components;

import org.fluentlenium.core.Fluent;
import org.fluentlenium.core.domain.FluentWebElement;

public class Drawer {

    private final FluentWebElement element;

    public Drawer(final FluentWebElement element) {
        this.element = element;
    }

    public static Drawer from(final Fluent fluent) {
        return new Drawer(fluent.findFirst("nav"));
    }

    public DrawerGroup group(final String name) {
        return new DrawerGroup(this.element.findFirst("#projects > li[data-group=" + name + "]"));
    }
}