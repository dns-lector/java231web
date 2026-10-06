package step.learning.java231web.data.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 *
 * @author Lector
 */
public class Product {
    private UUID id;
    private UUID sectionId;
    private String title;
    private String description;
    private String slug;
    private String imageUrl;
    private Date deletedAt;
    private long orderInPrice;
    
    public static Product fromResultSet(ResultSet rs) throws SQLException {
        Timestamp t = rs.getTimestamp("product_deleted_at");
        String parentId = rs.getString("product_section_id");
        return new Product()
                .setId( UUID.fromString(rs.getString("product_id")) )
                .setSectionId( parentId == null ? null : UUID.fromString(parentId) )
                .setTitle(rs.getString("product_title"))
                .setDescription(rs.getString("product_description"))
                .setSlug(rs.getString("product_slug"))
                .setImageUrl(rs.getString("product_image_url"))
                .setDeletedAt(t == null ? null : new Date(t.getTime()))
                .setOrderInPrice(rs.getLong("product_order"));
    }

    public UUID getId() {
        return id;
    }

    public Product setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getSectionId() {
        return sectionId;
    }

    public Product setSectionId(UUID sectionId) {
        this.sectionId = sectionId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Product setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public Product setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public Product setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public long getOrderInPrice() {
        return orderInPrice;
    }

    public Product setOrderInPrice(long orderInPrice) {
        this.orderInPrice = orderInPrice;
        return this;
    }

}
