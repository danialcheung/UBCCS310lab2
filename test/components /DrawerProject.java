package components;

import org.fluentlenium.core.domain.FluentWebElement;

import com.google.common.base.Predicate;

public class DrawerProject {
    private final FluentWebElement element;

    public DrawerProject(final FluentWebElement element) {
        this.element = element;
    }

    public String name() {
        final FluentWebElement a = this.element.findFirst("a.name");
        if (a.isDisplayed()) {
            return a.getText().trim();
        } else {
            return this.element.findFirst("input").getValue().trim();
        }
    }

    public void rename(final String newName) {
        this.element.findFirst(".name").doubleClick();
        this.element.findFirst("input").text(newName);
        this.element.click();
    }

    public Predicate isInEdit() {
        return new Predicate() {
            @Override
            public boolean apply(final Object o) {
                return DrawerProject.this.element.findFirst("input") != null
                    && DrawerProject.this.element.findFirst("input").isDisplayed();
            }
        };
    }
}