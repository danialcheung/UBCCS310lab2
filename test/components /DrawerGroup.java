package components;

import java.util.ArrayList;
import java.util.List;

import org.fluentlenium.core.domain.FluentWebElement;

import com.google.common.base.Predicate;

public class DrawerGroup {
    private final FluentWebElement element;

    public DrawerGroup(final FluentWebElement element) {
        this.element = element;
    }

    public List projects() {
        final List projects = new ArrayList();
        for (final FluentWebElement e : this.element.find("ul > li")) {
        projects.add(new DrawerProject(e));
    }
    return projects;
    }

    public DrawerProject project(final String name) {
    for (final DrawerProject p : this.projects()) {
        if (p.name().equals(name)) {
            return p;
            }
    }
    return null;
    }

    public Predicate hasProject(final String name) {
    return new Predicate() {
        @Override
        public boolean apply(final Object o) {
            return DrawerGroup.this.project(name) != null;
        }
    };
    }

    public void newProject() {
        this.element.findFirst(".newProject").click();
    }
}