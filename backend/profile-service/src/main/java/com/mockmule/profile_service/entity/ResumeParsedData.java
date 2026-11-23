package com.mockmule.profile_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;


@Entity
@Table(name = "resume_parsed_data")
public class ResumeParsedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToMany(mappedBy = "parsedData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "parsedData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeExperience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "parsedData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeEducation> educations = new ArrayList<>();

    @OneToMany(mappedBy = "parsedData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeProject> projects = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "resume_id", nullable = false, unique = true)
    private Resume resume;

    @Column(length = 3000)
    private String professionalSummary;

    private LocalDateTime extractedAt;

    @Column(length = 50)
    private String parserVersion;

    @PrePersist
    protected void onCreate(){
        extractedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public String getProfessionalSummary() {
        return professionalSummary;
    }

    public void setProfessionalSummary(String professionalSummary) {
        this.professionalSummary = professionalSummary;
    }

    public LocalDateTime getExtractedAt() {
        return extractedAt;
    }

    public void setExtractedAt(LocalDateTime extractedAt) {
        this.extractedAt = extractedAt;
    }

    public String getParserVersion() {
        return parserVersion;
    }

    public void setParserVersion(String parserVersion) {
        this.parserVersion = parserVersion;
    }

    public List<ResumeSkill> getSkills() {
        return skills;
    }

    public void setSkills(List<ResumeSkill> skills) {
        this.skills = skills;
    }

    public List<ResumeExperience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<ResumeExperience> experiences) {
        this.experiences = experiences;
    }

    public List<ResumeEducation> getEducations() {
        return educations;
    }

    public void setEducations(List<ResumeEducation> educations) {
        this.educations = educations;
    }

    public List<ResumeProject> getProjects() {
        return projects;
    }

    public void setProjects(List<ResumeProject> projects) {
        this.projects = projects;
    }
}
