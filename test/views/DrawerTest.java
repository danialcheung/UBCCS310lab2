package views;

import org.junit.Before;
import org.junit.Test;

import pages.Dashboard;
import pages.Login;
import play.test.WithBrowser;

import com.google.common.base.Predicates;
import components.Drawer;

public class DrawerTest extends WithBrowser {

    public Drawer drawer;
    public Dashboard dashboard;

    @Before
    public void setUp() {
        super.startServer();
        final Login login = this.browser.createPage(Login.class);
        login.go();
        login.login("bob@example.com", "secret");
        this.dashboard = this.browser.createPage(Dashboard.class);
       this.drawer = this.dashboard.drawer();
    }

    @Test
public void newProject() throws Exception {
    this.drawer.group("Personal").newProject();
    this.dashboard.await().until(this.drawer.group("Personal").hasProject("New project"));
    this.dashboard.await().until(
    this.drawer.group("Personal").project("New project").isInEdit());
}


@Test
public void renameProject() throws Exception {
    this.drawer.group("Personal").project("Private").rename("Confidential");
    this.dashboard.await().until(
    Predicates.not(this.drawer.group("Personal").hasProject("Private")));
    this.dashboard.await().until(this.drawer.group("Personal").hasProject("Confidential"));
    this.dashboard.await().until(
    Predicates.not(this.drawer.group("Personal").project("Confidential").isInEdit()));
}

}