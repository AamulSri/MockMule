package com.mockmule.profile_service.service;

import com.mockmule.profile_service.dto.CreateProfileRequest;
import com.mockmule.profile_service.dto.ProfileResponse;
import com.mockmule.profile_service.dto.UpdateProfileRequest;
import com.mockmule.profile_service.entity.Profile;

import java.util.Optional;

public interface ProfileService {

    ProfileResponse createProfile(Long UserId , CreateProfileRequest request);
    ProfileResponse getProfileByUserId(Long userId);
    ProfileResponse updateProfile(Long userId, UpdateProfileRequest request);
}
