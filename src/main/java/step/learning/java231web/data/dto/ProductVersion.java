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
public class ProductVersion {
    private UUID id;
    private UUID productId;
    private String titleAddon;
    private String descriptionAddon;
    private String slugAddon;
    private String imageUrl;
    private Date deletedAt;
    private long orderInPrice;
    private double price;
    private long stock;
    
    public static ProductVersion fromResultSet(ResultSet rs) throws SQLException {
        Timestamp t = rs.getTimestamp("product_version_deleted_at");
        return new ProductVersion()
                .setId( UUID.fromString(rs.getString("product_version_id")) )
                .setProductId( UUID.fromString("product_id") )
                .setTitleAddon(rs.getString("product_version_title_addon"))
                .setDescriptionAddon(rs.getString("product_version_description_addon"))
                .setSlugAddon(rs.getString("product_version_slug_addon"))
                .setImageUrl(rs.getString("product_version_image_url"))
                .setDeletedAt(t == null ? null : new Date(t.getTime()))
                .setOrderInPrice(rs.getLong("product_version_order"))
                .setPrice(rs.getDouble("product_version_price"))
                .setStock(rs.getLong("product_version_stock"));
    }

    public UUID getId() {
        return id;
    }

    public ProductVersion setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getProductId() {
        return productId;
    }

    public ProductVersion setProductId(UUID productId) {
        this.productId = productId;
        return this;
    }

    public String getTitleAddon() {
        return titleAddon;
    }

    public ProductVersion setTitleAddon(String titleAddon) {
        this.titleAddon = titleAddon;
        return this;
    }

    public String getDescriptionAddon() {
        return descriptionAddon;
    }

    public ProductVersion setDescriptionAddon(String descriptionAddon) {
        this.descriptionAddon = descriptionAddon;
        return this;
    }

    public String getSlugAddon() {
        return slugAddon;
    }

    public ProductVersion setSlugAddon(String slugAddon) {
        this.slugAddon = slugAddon;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ProductVersion setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public ProductVersion setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    public long getOrderInPrice() {
        return orderInPrice;
    }

    public ProductVersion setOrderInPrice(long orderInPrice) {
        this.orderInPrice = orderInPrice;
        return this;
    }

    public double getPrice() {
        return price;
    }

    public ProductVersion setPrice(double price) {
        this.price = price;
        return this;
    }

    public long getStock() {
        return stock;
    }

    public ProductVersion setStock(long stock) {
        this.stock = stock;
        return this;
    }
    
    
}
