package com.mockmule.profile_service.mapper;

import com.mockmule.profile_service.dto.ResumeResponse;
import com.mockmule.profile_service.entity.Resume;
import org.springframework.stereotype.*;

@Component
public class ResumeMapper {

    public ResumeResponse toResponse(Resume resume){
        return new ResumeResponse(resume.getId(),
                resume.getProfile().getId(),
                resume.getFileName(),
                resume.getFileType(),
                resume.getFileSize(),
                resume.getParsingStatus(),
                resume.getUploadedAt());
    }
}
