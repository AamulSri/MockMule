package com.mockmule.profile_service.dto;

import com.mockmule.profile_service.enums.ResumeParsingStatus;
import java.time.LocalDateTime;

public record ResumeResponse(
        Long id,
        Long profileId,
        String fileName,
        String fileType,
        Long fileSize,
        ResumeParsingStatus parsingStatus,
        LocalDateTime updatedAt

) {}
