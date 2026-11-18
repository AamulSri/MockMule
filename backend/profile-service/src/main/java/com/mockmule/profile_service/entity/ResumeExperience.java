package com.mockmule.profile_service.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name ="resume_experience",
        indexes = {
                @Index(name = "idx_resume_experience_parsed_data",
                columnList = "parsed_data_id")
        }
)
public class ResumeExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parsed_data_id", nullable = false)
    private ResumeParsedData parsedData;

    @Column(nullable = false, length = 150)
    private String companyName;

    @Column(nullable = false, length = 150)
    private String designation;

    @Column(length = 3000)
    private String responsibilities;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyWorking;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResumeParsedData getParsedData() {
        return parsedData;
    }

    public void setParsedData(ResumeParsedData parsedData) {
        this.parsedData = parsedData;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrentlyWorking() {
        return currentlyWorking;
    }

    public void setCurrentlyWorking(boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }
}
