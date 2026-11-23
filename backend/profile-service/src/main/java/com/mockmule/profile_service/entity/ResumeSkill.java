package com.mockmule.profile_service.entity;

import jakarta.persistence.*;

@Entity
@Table(
        name = "resume_skills",
        indexes = {
                @Index(
                        name = "idx_resume_skill_parsed_data",
                        columnList = "parsed_data_id"
                )
        }
)
public class ResumeSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parsed_data_id", nullable = false)
    private ResumeParsedData parsedData;

    @Column(length = 200, nullable = false)
    private String skillName;

    @Column(length = 50)
    private String category;

    @Column(length = 10)
    private String proficiency;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ResumeParsedData getResumeParsedData() {
        return parsedData;
    }

    public void setResumeParsedData(ResumeParsedData parsedData) {
        this.parsedData = parsedData;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProficiency() {
        return proficiency;
    }

    public void setProficiency(String proficiency) {
        this.proficiency = proficiency;
    }
}
