package step.learning.java231web.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import step.learning.java231web.data.dto.Product;
import step.learning.java231web.data.dto.ProductVersion;
import step.learning.java231web.services.db.IDbService;

/**
 *
 * @author Lector
 */
@Singleton
public class ProductDao {    
    private final IDbService dbService;
    private final Logger logger;   // Guice автоматично провадить Logger
    
    @Inject
    public ProductDao(IDbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }
    
    public UUID addProduct(Product product) throws SQLException {
        if(product.getId() == null) {
            product.setId( UUID.randomUUID() );
        }
        String sql = "INSERT INTO products(product_id, product_section_id,"
                + "product_title, product_description, product_slug,"
                + "product_image_url, product_order) VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, product.getId().toString());
            prep.setString(2, product.getSectionId().toString());
            prep.setString(3, product.getTitle());
            prep.setString(4, product.getDescription());
            prep.setString(5, product.getSlug());
            prep.setString(6, product.getImageUrl());
            prep.setLong(7, product.getOrderInPrice());
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, sql, ex );
            throw ex;
        }
        return product.getId();
    }
    
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS products ("
                + "product_id          CHAR(36)     NOT NULL  PRIMARY KEY,"
                + "product_section_id  CHAR(36)     NOT NULL,"
                + "product_title       VARCHAR(64)  NOT NULL,"
                + "product_description VARCHAR(512)     NULL,"
                + "product_slug        VARCHAR(64)      NULL  UNIQUE,"
                + "product_image_url   VARCHAR(128)     NULL,"
                + "product_order       BIGINT       DEFAULT 1000000,"
                + "product_deleted_at  DATETIME         NULL"
                + ")ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci";
        try( Statement statement = dbService.getConnection().createStatement() ) {
            statement.executeUpdate(sql);
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, sql, ex );
            throw ex;
        }
    }
}
