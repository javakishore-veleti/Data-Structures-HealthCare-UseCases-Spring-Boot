package com.jk.solutions.data_structures.health_care.plans_mgmt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @Column(name = "id", nullable = false, length = 64)
    private String id;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "A";

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, length = 64)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by", length = 64)
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    // Getters and setters can be added as needed

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = "BaseEntity";
        this.updatedBy = "BaseEntity";
        this.isDeleted = false;
    }
}
