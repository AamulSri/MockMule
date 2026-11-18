package com.mockmule.profile_service.entity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "resume_educations",
        indexes = {
                @Index(
                        name = "idx_resume_education_parsed_data",
                        columnList = "parsed_data_id"
                )
        }
)
public class ResumeEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parsed_data_id", nullable = false)
    private ResumeParsedData parsedData;

    @Column(nullable = false, length = 150)
    private String degree;

    @Column(nullable = false, length = 200)
    private String institution;

    @Column(length = 100)
    private String fieldOfStudy;

    private Integer startYear;

    private Integer graduationYear;

    @Column(length = 50)
    private String grade;

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

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
