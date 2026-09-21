package step.learning.java231web.services.db;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Lector
 */
@Singleton
public class MySqlDbService implements IDbService {
    private final Gson gson;
    private Connection connection = null;

    @Inject
    public MySqlDbService(Gson gson) {
        this.gson = gson;
    }
    
    @Override
    public Connection getConnection() throws SQLException {
        if(connection == null) {
            // Load config file
            try ( BufferedReader rdr = new BufferedReader(
                new InputStreamReader(
                    this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("db.json")))
            ) {           
                JsonObject json = gson
                        .fromJson(rdr, JsonElement.class)
                        .getAsJsonObject();
                String connectionString = String.format(
                        "%s:%s://%s:%d/%s?%s",
                        json.get("provider").getAsString(),
                        json.get("scheme").getAsString(),
                        json.get("host").getAsString(),
                        json.get("port").getAsInt(),
                        json.get("database").getAsString(),
                        json.get("params").getAsString()
                );
                MysqlDataSource dataSource = new MysqlDataSource();
                dataSource.setUrl(connectionString);
                connection = dataSource.getConnection(
                        json.get("user").getAsString(), 
                        json.get("password").getAsString()
                );                
            }
            catch(Exception ex) {
                throw new SQLException(ex);
            }
        }
        return connection;
    }

    @Override
    public boolean testConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void reconnect() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
