package step.learning.java231web.data.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

/**
 * Entity 'User'
 * @author Lector
 */
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private Date createdAt; 
    private Date deletedAt; 
    
    public static UserDto fromResultSet(ResultSet rs) throws SQLException {
        Timestamp t = rs.getTimestamp("deleted_at");
        return new UserDto()
                .setId(UUID.fromString(rs.getString("id")))
                .setName(rs.getString("name"))
                .setEmail(rs.getString("email"))
                .setCreatedAt(new Date(rs.getTimestamp("created_at").getTime()))
                .setDeletedAt(t == null ? null : new Date(t.getTime()));
    }

    public UUID getId() {
        return id;
    }

    public UserDto setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserDto setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public UserDto setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
    
}
/*
DAL - Data Access Layer - Шар доступу до даних
Складається з
DTO - Data Transfer Object(s) - вони ж Entities - 
класи, що описують структури даних, зазвичай, містять
поля (властивості) і не містять методів, окрім фабричних.
DAO - Data Access Object(s) - класи, що описують дії
з DTO (зазвичай, один клас DAO містить дії для одного DTO)
ці класи, навпаки, майже не містять полів - лише методи.
*/