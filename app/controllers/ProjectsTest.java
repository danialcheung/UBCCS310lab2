package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import models.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class ProjectsTest extends WithApplication {
@Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((Collection) Yaml.load("test-data.yml"));
    }

    @Test
public void newProject() {
    final Result result = route(fakeRequest("POST", "/projects").session("email",
        "bob@example.com").bodyForm(ImmutableMap.of("group", "Some Group")));
    assertEquals(200, result.status());
    final Project project = Project.find.where().eq("folder", "Some Group").findUnique();
    assertNotNull(project);
    assertEquals("New project", project.name);
    assertEquals(1, project.members.size());
    assertEquals("bob@example.com", project.members.get(0).email);
}

@Test
public void renameProject() {
    final long id = Project.find.where().eq("members.email", "bob@example.com")
        .eq("name", "Private").findUnique().id;
    final Result result = route(fakeRequest("PUT", "/projects/" + id).session("email",
        "bob@example.com").bodyForm(ImmutableMap.of("name", "New name")));
    assertEquals(200, result.status());
    assertEquals("New name", Project.find.byId(id).name);
}

@Test
public void renameProjectForbidden() {
    final long id = Project.find.where().eq("members.email", "bob@example.com")
        .eq("name", "Private").findUnique().id;
    final Result result = route(fakeRequest("PUT", "/projects/" + id).session("email",
        "jeff@example.com").bodyForm(ImmutableMap.of("name", "New name")));
    assertEquals(403, result.status());
}

}