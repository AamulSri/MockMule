
package com.mockmule.profile_service.dto;

import java.time.LocalDateTime;

public record ProfileResponse(

        Long id,
        Long userId,
        String fullName,
        String phoneNumber,
        String location,
        String headline,
        String summary,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}
