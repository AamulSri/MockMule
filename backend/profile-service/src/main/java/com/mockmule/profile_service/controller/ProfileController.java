package com.mockmule.profile_service.controller;

import com.mockmule.profile_service.dto.ApiResponse;
import com.mockmule.profile_service.dto.CreateProfileRequest;
import com.mockmule.profile_service.dto.ProfileResponse;
import com.mockmule.profile_service.dto.UpdateProfileRequest;
import com.mockmule.profile_service.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/profle")
public class ProfileController {
    private final ProfileService profileService;

    public ProfileController (ProfileService profileService){
        this.profileService = profileService;
    }

    //get , create , update.

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileByUserId(){

        // Temporary userId until JWT authentication is integrated.
        Long userId = 101L;
        ProfileResponse profile = profileService.getProfileByUserId(userId);
        ApiResponse<ProfileResponse> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Profile retrived Successfully",
                profile,
                null
        );

        return ResponseEntity.ok(apiResponse);

    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(@Valid @RequestBody CreateProfileRequest createProfileRequest){
        Long userId = 101L;
        ProfileResponse profile = profileService.createProfile(userId,createProfileRequest);
        ApiResponse<ProfileResponse> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Profile Created Successfully",
                profile,
                null
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile( @Valid @RequestBody UpdateProfileRequest updateProfileRequest){
        Long userId = 101L;
        ProfileResponse profile = profileService.updateProfile(userId, updateProfileRequest);
        ApiResponse<ProfileResponse> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Profile Updated Successfully",
                profile,
                null
        );
        return ResponseEntity.ok(apiResponse);

    }


}
