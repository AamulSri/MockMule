package com.mockmule.profile_service.entity;
import jakarta.persistence.*;

@Entity
@Table(
        name = "resume_projects",
        indexes = {
                @Index(
                        name = "idx_resume_project_parsed_data",
                        columnList = "parsed_data_id"
                )
        }
)
public class ResumeProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parsed_data_id", nullable = false)
    private ResumeParsedData parsedData;

    @Column(nullable = false, length = 200)
    private String projectName;

    @Column(length = 3000)
    private String description;

    @Column(length = 1000)
    private String technologies;

    @Column(length = 500)
    private String projectUrl;


    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechnologies() {
        return technologies;
    }

    public void setTechnologies(String technologies) {
        this.technologies = technologies;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }
}
