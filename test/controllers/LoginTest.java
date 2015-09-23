package controllers;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

import play.mvc.*;
import play.libs.*;
import play.test.*;
import static play.test.Helpers.*;
import com.avaje.ebean.Ebean;
import com.google.common.collect.ImmutableMap;

public class LoginTest extends WithApplication {
    @Before
    public void setUp() {
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((Collection) Yaml.load("test-data.yml"));
    }

    @Test
public void authenticateSuccess() {
    final Result result = route(fakeRequest("POST", "/login").bodyForm(ImmutableMap.of("email", "bob@example.com", "password", "secret")));
    System.out.println(fakeRequest("POST", "./login").path());
    assertEquals(303, result.status());
    assertEquals("bob@example.com", result.session().get("email"));
}

@Test
public void authenticateFailure() {
    final Result result = route(fakeRequest("POST", "/login").bodyForm(ImmutableMap.of("email", "bob@example.com", "password", "badpassword")));
    System.out.println(fakeRequest("POST", "./login").path());
    assertEquals(400, result.status());
    assertNull(result.session().get("email"));
}

@Test
public void authenticated() {
    final Result result = route(fakeRequest("GET", "/").session("email", "bob@example.com"));
    assertEquals(200, result.status());
}

@Test
public void notAuthenticated() {
    final Result result = route(fakeRequest("GET", "/"));
    assertEquals(303, result.status());
    assertEquals("/login", result.header("location"));
}


}