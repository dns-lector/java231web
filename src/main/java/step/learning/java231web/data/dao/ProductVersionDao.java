package step.learning.java231web.data.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import step.learning.java231web.data.dto.ProductVersion;
import step.learning.java231web.services.db.IDbService;

/**
 *
 * @author Lector
 */
@Singleton
public class ProductVersionDao {
    private final IDbService dbService;
    private final Logger logger;   // Guice автоматично провадить Logger
    
    @Inject
    public ProductVersionDao(IDbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }
    
    public UUID addProductVersion(ProductVersion productVersion) throws SQLException {
        if(productVersion.getId() == null) {
            productVersion.setId( UUID.randomUUID() );
        }
        String sql = "INSERT INTO product_versions("
                + "product_version_id, "                  // 1
                + "product_id, "                          // 2
                + "product_version_title_addon, "         // 3
                + "product_version_description_addon, "   // 4
                + "product_version_slug_addon,"           // 5
                + "product_version_image_url, "           // 6
                + "product_version_order,"                // 7        
                + "product_version_price, "               // 8
                + "product_version_stock) "               // 9
                + "VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, productVersion.getId().toString());
            prep.setString(2, productVersion.getProductId().toString());
            prep.setString(3, productVersion.getTitleAddon());
            prep.setString(4, productVersion.getDescriptionAddon());
            prep.setString(5, productVersion.getSlugAddon());
            prep.setString(6, productVersion.getImageUrl());
            prep.setLong(7, productVersion.getOrderInPrice());
            prep.setDouble(8, productVersion.getPrice());
            prep.setLong(9, productVersion.getStock());
            prep.executeUpdate();
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, sql, ex );
            throw ex;
        }
        return productVersion.getId();
    }
    
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS product_versions ("
                + "product_version_id                CHAR(36)      NOT NULL  PRIMARY KEY,"
                + "product_id                        CHAR(36)      NOT NULL,"
                + "product_version_title_addon       VARCHAR(64)       NULL,"
                + "product_version_description_addon VARCHAR(512)      NULL,"
                + "product_version_slug_addon        VARCHAR(32)       NULL,"
                + "product_version_image_url         VARCHAR(128)      NULL,"
                + "product_version_order             BIGINT        DEFAULT 1000000,"
                + "product_version_price             DECIMAL(18,2) NOT NULL ,"
                + "product_version_stock             BIGINT        DEFAULT 1,"
                + "product_version_deleted_at        DATETIME          NULL"
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
