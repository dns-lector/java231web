package step.learning.java231web.data.dto;

import java.util.UUID;

/**
 *
 * @author Lector
 */
public class UserAccess {
    private UUID id;
    private UUID userId;
    private String login;
    private String salt;
    private String dk;
    private String role;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getDk() {
        return dk;
    }

    public void setDk(String dk) {
        this.dk = dk;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
