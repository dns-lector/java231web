package step.learning.java231web.data.dto;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
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