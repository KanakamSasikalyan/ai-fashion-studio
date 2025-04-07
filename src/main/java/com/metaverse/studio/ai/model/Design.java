package com.metaverse.studio.ai.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "designs")
public class Design {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String prompt;

    private String description;

    @Column(nullable = false)
    private String style;

    @Lob
    @Column(nullable = false)
    private byte[] imageData;

    @Lob
    private byte[] thumbnail;

    @Column(nullable = false)
    private Boolean isPublic = true;

    private UUID creatorId;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private Integer likes = 0;

    public Design() {

    }

    public Design(String prompt, String description, String style, byte[] imageData, byte[] thumbnail, Boolean isPublic, UUID creatorId, LocalDateTime createdAt, Integer likes) {
        this.prompt = prompt;
        this.description = description;
        this.style = style;
        this.imageData = imageData;
        this.thumbnail = thumbnail;
        this.isPublic = isPublic;
        this.creatorId = creatorId;
        this.createdAt = createdAt;
        this.likes = likes;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getPublic() {
        return isPublic;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public UUID getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(UUID creatorId) {
        this.creatorId = creatorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }
}