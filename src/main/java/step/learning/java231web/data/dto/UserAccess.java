package step.learning.java231web.data.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    public static UserAccess fromResultSet(ResultSet rs) throws SQLException {
        if(rs.next()) {
            UserAccess ua = new UserAccess();
            ua.setId( UUID.fromString(rs.getString("id")) );
            ua.setUserId( UUID.fromString(rs.getString("user_id")) );
            ua.setLogin( rs.getString("login") );
            ua.setSalt( rs.getString("salt") );
            ua.setDk( rs.getString("dk") );
            ua.setRole( rs.getString("role") );
            return ua;
        }
        else return null;
    }

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
