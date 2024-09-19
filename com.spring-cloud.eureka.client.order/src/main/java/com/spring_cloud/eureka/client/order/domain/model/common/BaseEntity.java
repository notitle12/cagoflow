package com.spring_cloud.eureka.client.order.domain.model.common;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "created_by", updatable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false; // 논리 삭제 여부


    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // 논리 삭제 여부 확인 메서드
    public boolean isDeletedSoftly() {
        return isDelete;
    }

    // 논리 삭제 처리 메서드
    public void deleteSoftly(String deletedBy) {
        this.isDelete = true;
        this.deletedBy = deletedBy;
        this.deletedAt = LocalDateTime.now();
    }

    // 논리 삭제 취소 메서드
    public void undoDelete() {
        this.isDelete = false;
        this.deletedBy = null;
        this.deletedAt = null;
    }

}
