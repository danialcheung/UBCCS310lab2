package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import com.avaje.ebean.Model;

@Entity
public class User extends Model {

    @Id
    public String email;
    public String name;
    public String password;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public static Finder<String, User> find = new Finder(User.class);
    public static User authenticate(String email, String password) { 
        return find.where().eq("email", email) .eq("password", password).findUnique(); }
}