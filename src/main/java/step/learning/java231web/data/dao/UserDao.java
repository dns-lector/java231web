package step.learning.java231web.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import step.learning.java231web.data.dto.UserAccess;
import step.learning.java231web.data.dto.UserDto;
import step.learning.java231web.data.models.UserSignupFormModel;
import step.learning.java231web.rest.RestPagination;
import step.learning.java231web.services.db.IDbService;
import step.learning.java231web.services.kdf.IKdfService;

/**
 *
 * @author Lector
 */
@Singleton
public class UserDao {
    private final IDbService dbService;
    private final Logger logger;   // Guice автоматично провадить Logger
    private final IKdfService kdfService;
    
    @Inject
    public UserDao(IDbService dbService, Logger logger, IKdfService kdfService) {
        this.dbService = dbService;
        this.logger = logger;
        this.kdfService = kdfService;
    }
    
    public long getUsersCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users";
        try(Statement stmt = dbService.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            rs.next();
            return rs.getLong(1);
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public List<UserDto> getUsers(RestPagination pagination) throws SQLException {
        // Задача пагінації у запитах вирішується їх обмеженням:
        // задається кількість елементів у вибірці (LIMIT)
        // та кількість зміщення (OFFSET)
        // Також рекомендується додавати впорядкування за часом
        // створення або залежним від нього полем
        
        String sql = String.format(
                "SELECT * FROM users ORDER BY created_at  LIMIT %d OFFSET %d",
                pagination.getPageSize(),
                pagination.getPageSize() * (pagination.getPage() - 1)
        );
        try(Statement stmt = dbService.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            List<UserDto> ret = new ArrayList<>();
            while(rs.next()) {
                ret.add( UserDto.fromResultSet(rs) );
            }
            return ret;
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public UserAccess authenticate(String login, String password) throws SQLException {
        String sql = "SELECT * FROM user_accesses ua WHERE ua.login = ?";
        UserAccess ua = null;
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, login);
            try( ResultSet rs = prep.executeQuery() ){
                ua = UserAccess.fromResultSet(rs);
            }            
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
        if(ua != null) {
            String dk = kdfService.dk( password, ua.getSalt() );
            if( ! dk.equals( ua.getDk() ) ) {
                ua = null;
            }
        }
        return ua;
    }
    
    public void signupUser(UserSignupFormModel formModel) throws SQLException {
        signupUser(formModel, "guest");    
    }
    
    public void signupUser(UserSignupFormModel formModel, String role) throws SQLException {
        // Д.З. Реалізувати валідацію моделі форми реєстрації користувача
        String sql = "INSERT INTO users(id, name, email) VALUES (?, ?, ?)";
        String userId = UUID.randomUUID().toString();
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, userId);
            prep.setString(2, formModel.getName());
            prep.setString(3, formModel.getEmail());
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
        sql = "INSERT INTO user_accesses(id, user_id, `login`, salt, dk, role)"
                + " VALUES (UUID(), ?, ?, ?, ?, ?)";
        String salt = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16);
        String dk = kdfService.dk(formModel.getPassword(), salt);
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, userId);
            prep.setString(2, formModel.getLogin());
            prep.setString(3, salt);
            prep.setString(4, dk);
            prep.setString(5, role);
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public void seedData() throws SQLException {
        String sql = "INSERT INTO users (id, name, email) "
                + "VALUES ('2d218599-b652-11f1-828e-62517600596c',"
                + " 'Default Administrator', 'change_me@some.mail') "
                + "ON DUPLICATE KEY UPDATE "
                + "name = 'Default Administrator', "
                + "email = 'change_me@some.mail'";
        try(Statement stmt = dbService.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
        sql = "INSERT INTO user_accesses(id, user_id, `login`, salt, dk, role)"
                + " VALUES ("
                + "'381548f8-b653-11f1-828e-62517600596c', "
                + "'2d218599-b652-11f1-828e-62517600596c', "
                + "'admin', ?, ?, 'admin') "
                + "ON DUPLICATE KEY UPDATE "
                + "user_id = '2d218599-b652-11f1-828e-62517600596c', "
                + "`login` = 'admin', "
                + "role = 'admin', "
                + "salt = ?,"
                + "dk = ?";
        String salt = UUID
                .randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 16);
        String dk = kdfService.dk("root", salt);
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, salt);
            prep.setString(2, dk);
            prep.setString(3, salt);
            prep.setString(4, dk);
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id CHAR(36) NOT NULL PRIMARY KEY,"
                + "`name` VARCHAR(64) NOT NULL,"
                + "email VARCHAR(128),"
                + "created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                + "deleted_at DATETIME"
                + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci";
        try( Statement statement = dbService.getConnection().createStatement() ) {
            statement.executeUpdate(sql);
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
        sql = "CREATE TABLE IF NOT EXISTS user_accesses ("
                + "id      CHAR(36) NOT NULL PRIMARY KEY,"
                + "user_id CHAR(36) NOT NULL,"
                + "`login` VARCHAR(64) NOT NULL UNIQUE,"
                + "salt    CHAR(16) NOT NULL,"
                + "dk      CHAR(32) NOT NULL,"
                + "role    VARCHAR(64)"
                + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci";
        try( Statement statement = dbService.getConnection().createStatement() ) {
            statement.executeUpdate(sql);
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
}
/*
User     UserAccess
name      userId
email     login
          salt, dk

Д.З. Додати до валідації форми реєстрації користувача
перевірку на унікальність (доступність) логіна.
Повторити REST з ASP
*/