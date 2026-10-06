package step.learning.java231web.data.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Product group (section)
 * @author Lector
 */
public class ProductSection {
    private UUID id;
    private UUID parentId;
    private String title;
    private String description;
    private String slug;
    private String imageUrl;
    private Date deletedAt;
    private long orderInPrice;
    
    public static ProductSection fromResultSet(ResultSet rs) throws SQLException {
        Timestamp t = rs.getTimestamp("product_section_deleted_at");
        String parentId = rs.getString("product_section_parent_id");
        return new ProductSection()
                .setId( UUID.fromString(rs.getString("product_section_id")) )
                .setParentId( parentId == null ? null : UUID.fromString(parentId) )
                .setTitle(rs.getString("product_section_title"))
                .setDescription(rs.getString("product_section_description"))
                .setSlug(rs.getString("product_section_slug"))
                .setImageUrl(rs.getString("product_section_image_url"))
                .setDeletedAt(t == null ? null : new Date(t.getTime()))
                .setOrderInPrice(rs.getLong("product_section_order"));
    }

    public UUID getId() {
        return id;
    }

    public ProductSection setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getParentId() {
        return parentId;
    }

    public ProductSection setParentId(UUID parentId) {
        this.parentId = parentId;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ProductSection setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ProductSection setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSlug() {
        return slug;
    }

    public ProductSection setSlug(String slug) {
        this.slug = slug;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductSection setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public ProductSection setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public long getOrderInPrice() {
        return orderInPrice;
    }

    public ProductSection setOrderInPrice(long orderInPrice) {
        this.orderInPrice = orderInPrice;
        return this;
    }
    
    
}
/*
[ProductSections]       [Product]             [ProductVersion]
UUID id                  UUID id                UUID id
UUID parentId;           UUID sectionId;        UUID productId;
String title;            String title;          Double price
String description;      String description;    String? titleAddon
String slug;             String slug;           String? descriptionAddon
String imageUrl;         String imageUrl;       String? imageUrl;
Date deletedAt;          Date deletedAt;        Date deletedAt;
long orderInPrice;       long orderInPrice;     long orderInPrice;
                                                long stock

*/