package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import static play.data.Form.*;

import models.*;
import views.html.*;

public class Application extends Controller {

    @Security.Authenticated(Secured.class)

    public Result index() {
        return ok(index.render( Project.findInvolving(request().username()),
    Task.findTodoInvolving(request().username()),
    User.find.byId(request().username())));
    }

public Result login() {
    return ok(
        login.render(Form.form(Login.class))
    );
}

public static class Login {

  public String email;
  public String password;

public String validate() {
    if (User.authenticate(email, password) == null) {
        return "Invalid user or password";
    }
    return null;
}

}


public Result authenticate() {
    Form<Login> loginForm = Form.form(Login.class).bindFromRequest();
    if (loginForm.hasErrors()) {
        return badRequest(login.render(loginForm));
    } else {
        session().clear();
        session("email", loginForm.get().email);
        return redirect(routes.Application.index());
    }
}

public Result logout() {
    session().clear();
    flash("success", "You've been logged out");
    return redirect(routes.Application.login());
}

public Result javascriptRoutes() {
    response().setContentType("text/javascript");
    return ok(play.Routes.javascriptRouter("jsRoutes", routes.javascript.Projects.add(),
        routes.javascript.Projects.delete(), routes.javascript.Projects.rename(),
        routes.javascript.Projects.addGroup()));
}

}
