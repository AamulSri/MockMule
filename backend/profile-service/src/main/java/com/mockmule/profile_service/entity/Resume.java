package com.mockmule.profile_service.entity;


import com.mockmule.profile_service.enums.ResumeParsingStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
    name ="resumes",
    indexes = {
        @Index(name = "idx_resume_profile_id", columnList = "profile_id")
        }
)

public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="profile_id", nullable = false)
    private Profile profile;

    @Column(nullable = false , length = 255)
    private String fileName;

    @Column(nullable = false, length =20)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false , length = 20)
    private ResumeParsingStatus parsingStatus = ResumeParsingStatus.UPLOADED;

    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadeddAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Column(nullable = false, unique = true, length = 500)
    private String storageKey;

    @PrePersist
    protected void onCreate(){
        LocalDateTime now = LocalDateTime.now();
        uploadeddAt = now;
        updateAt= now;
    }

    @PostUpdate
    protected void onUpdate(){
        updateAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public ResumeParsingStatus getParsingStatus() {
        return parsingStatus;
    }

    public void setParsingStatus(ResumeParsingStatus parsingStatus) {
        this.parsingStatus = parsingStatus;
    }

    public LocalDateTime getUploadeddAt() {
        return uploadeddAt;
    }

    public void setUploadeddAt(LocalDateTime uploadeddAt) {
        this.uploadeddAt = uploadeddAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }
}
