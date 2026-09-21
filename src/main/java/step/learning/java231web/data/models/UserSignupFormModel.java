package step.learning.java231web.data.models;

import jakarta.servlet.http.HttpServletRequest;

/**
 *
 * @author Lector
 */
public class UserSignupFormModel {
    private String name;
    private String email;
    private String login;
    private String password;
    
    public static UserSignupFormModel fromRequest(HttpServletRequest req) {
        return new UserSignupFormModel()
                .setEmail( req.getParameter("email") )
                .setLogin( req.getParameter("login") )
                .setName( req.getParameter("name") )
                .setPassword( req.getParameter("password") );
    }

    public String getName() {
        return name;
    }

    public UserSignupFormModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserSignupFormModel setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public UserSignupFormModel setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserSignupFormModel setPassword(String password) {
        this.password = password;
        return this;
    }
    
}
/*
Дані, що приходять з фронтенду, не завжди один-до-одного
збігаються з DTO, більш того, можуть розщеплюватись на 
декілька об'єктів:
                      UserDto
UserSignupFormModel <
                      UserAccess
*/