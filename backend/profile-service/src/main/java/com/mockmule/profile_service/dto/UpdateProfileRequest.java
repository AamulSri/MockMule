package com.mockmule.profile_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @NotBlank(message = "Full name is required")
        @Size(max = 100)
        String fullName,

        @Size(max = 20)
        String phoneNumber,

        @Size(max = 100)
        String location,

        @Size(max = 150)
        String headline,

        @Size(max = 2000)
        String summary

) {}
