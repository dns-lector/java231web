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
import step.learning.java231web.data.dto.ProductSection;
import step.learning.java231web.rest.RestPagination;
import step.learning.java231web.services.db.IDbService;

/**
 * Data Accessor for ProductSection DTO
 * @author Lector
 */
@Singleton
public class ProductSectionDao {    
    private final IDbService dbService;
    private final Logger logger;   // Guice автоматично провадить Logger
    
    @Inject
    public ProductSectionDao(IDbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }
    
    public long getProductsCountBySectionSlug(String slug) throws SQLException {
        String sql = "select count(*) from products p " +
        "join product_sections ps on p.product_section_id = ps.product_section_id " +
        "where ps.product_section_slug = ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, slug);
            try( ResultSet rs = prep.executeQuery() ) {
                rs.next();
                return rs.getLong(1);                
            }
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public ProductSection getSectionBySlug(String slug, RestPagination pagination) throws SQLException {
        ProductSection productSection = null;
        String sql = "SELECT * FROM product_sections ps "
                + "LEFT JOIN products p ON ps.product_section_id = p.product_section_id "
                + "WHERE ps.product_section_slug = ? "
                + "ORDER BY p.product_order LIMIT ? OFFSET ?";
        try(PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, slug);
            prep.setLong(2, pagination.getPageSize());
            prep.setLong(3, pagination.getPageSize() * (pagination.getPage() - 1));
            try( ResultSet rs = prep.executeQuery() ) {
                if(rs.next()) {
                    productSection = ProductSection.fromResultSet(rs, true);
                }
            }
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
        // заповнюємо дані про дочірні розділи
        if(productSection != null) {
            sql = String.format( "SELECT * FROM product_sections ps "
                    + "WHERE ps.product_section_parent_id = '%s'",
                    productSection.getId()
            );
            try(Statement statement = dbService.getConnection().createStatement();
                ResultSet rs = statement.executeQuery(sql) ) {
                while(rs.next()) {
                    productSection.getChildren()
                            .add(ProductSection.fromResultSet(rs));
                }
            }
        }
        return productSection;
    }
        
    public long getSectionsCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM product_sections";
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
    
    public List<ProductSection> getSections() throws SQLException {
        return getSections(null);
    }
    
    public List<ProductSection> getSections(RestPagination pagination) throws SQLException {
        long pageSize = pagination == null ? 1000 
                : pagination.getPageSize();
        long offset = pagination == null ? 0 
                : pagination.getPageSize() * (pagination.getPage() - 1);
        
        String sql = String.format(
                "SELECT * FROM product_sections ORDER BY product_section_order  LIMIT %d OFFSET %d",
                pageSize,
                offset
        );
        try(Statement stmt = dbService.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        ) {
            List<ProductSection> ret = new ArrayList<>();
            while(rs.next()) {
                ret.add( ProductSection.fromResultSet(rs) );
            }
            return ret;
        }
        catch( SQLException ex ) {
            logger.log(Level.WARNING, sql, ex);
            throw ex;
        }
    }
    
    public UUID addProductSection(ProductSection ps) throws SQLException {
        if( ps.getId() == null ) {
            ps.setId( UUID.randomUUID() );
        }
        String sql = "INSERT INTO product_sections (product_section_id,"
                + "product_section_parent_id, product_section_title,"
                + "product_section_description, product_section_slug,"
                + "product_section_image_url, product_section_order) "
                + "VALUES(?,?,?,?,?,?,?)";
        
        try(PreparedStatement prep = 
                dbService.getConnection().prepareStatement(sql)
        ) {
            prep.setString( 1, ps.getId().toString() );
            UUID parentId = ps.getParentId();
            if(parentId != null) {
                prep.setString( 2, parentId.toString() );
            }
            else {
                prep.setString( 2, null );
            }
            prep.setString( 3, ps.getTitle() );
            prep.setString( 4, ps.getDescription() );
            prep.setString( 5, ps.getSlug() );
            prep.setString( 6, ps.getImageUrl() );
            prep.setLong( 7, ps.getOrderInPrice() );
            prep.executeUpdate() ;
        }
        catch( SQLException ex ) {
            logger.log( Level.WARNING, sql, ex );
            throw ex;
        }
        return ps.getId() ;
    }
    
    public void seedData() throws SQLException {
        addProductSection(
                new ProductSection()
                .setId(UUID.fromString("3a27db1d-d97e-406f-a36a-148b4f724a6e"))
                .setTitle("Garminfull")
                .setDescription("garminfull")
                .setSlug("garminfull")
                .setImageUrl("84dd3b41-e654-4c64-b784-25031bf675ed.webp")
                .setOrderInPrice(10)
        );
        addProductSection(
                new ProductSection()
                .setId(UUID.fromString("1fb04898-3225-422a-8592-37a17b74ad8c"))
                .setTitle("Телевізори")
                .setDescription("Телевізори")
                .setSlug("tv")
                .setImageUrl("f6ffd9f8-75b6-4a97-9abd-85733178b9ff.webp")
                .setOrderInPrice(20)
        );
        addProductSection(
                new ProductSection()
                .setId(UUID.fromString("ef8c86dd-3aed-4a94-b192-4e90c22a4fe9"))
                .setTitle("Консолі")
                .setDescription("Ігрові консолі")
                .setSlug("consoles")
                .setImageUrl("3632c102-86f1-4b01-9c34-8af14967c003.webp")
                .setOrderInPrice(30)
        );
        addProductSection(
                new ProductSection()
                .setId(UUID.fromString("3312c6ce-621b-4336-8158-515f7988cc4b"))
                .setTitle("б/у Apple")
                .setDescription("Техніка Apple що була у вжитку")
                .setSlug("applebw")
                .setImageUrl("d7ac61ab-197d-4815-818c-d0b07ed6bef5.webp")
                .setOrderInPrice(40)
        );
    }
    
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS product_sections ("
                + "product_section_id          CHAR(36)     NOT NULL  PRIMARY KEY,"
                + "product_section_parent_id   CHAR(36)         NULL,"
                + "product_section_title       VARCHAR(64)  NOT NULL,"
                + "product_section_description VARCHAR(512) NOT NULL,"
                + "product_section_slug        VARCHAR(32)  NOT NULL  UNIQUE,"
                + "product_section_image_url   VARCHAR(128) NOT NULL,"
                + "product_section_order       BIGINT       DEFAULT 1000000,"
                + "product_section_deleted_at  DATETIME         NULL"
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
