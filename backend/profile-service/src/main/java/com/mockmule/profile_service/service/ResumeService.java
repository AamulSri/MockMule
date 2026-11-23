package com.mockmule.profile_service.service;

import com.mockmule.profile_service.dto.ResumeResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ResumeService {
    ResumeResponse uploadResume(Long profileId, MultipartFile file);
    List<ResumeResponse> getResumesByProfileId(Long profileId);
    Resource downloadResume(Long profileId, Long resumeId);
    void deleteResume(Long profileId, Long resumeId);
}
