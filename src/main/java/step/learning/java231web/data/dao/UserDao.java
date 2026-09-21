package step.learning.java231web.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import step.learning.java231web.data.models.UserSignupFormModel;
import step.learning.java231web.services.db.IDbService;

/**
 *
 * @author Lector
 */
@Singleton
public class UserDao {
    private final IDbService dbService;
    private final Logger logger;   // Guice автоматично провадить Logger

    @Inject
    public UserDao(IDbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }
    
    public void signupUser(UserSignupFormModel formModel) throws SQLException {
        // Д.З. Реалізувати валідацію моделі форми реєстрації користувача
        String sql = "INSERT INTO user(id, name, email) VALUES (?, ?, ?)";
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
                + "`login` VARCHAR(64) NOT NULL,"
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
*/