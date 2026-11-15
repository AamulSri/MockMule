package com.mockmule.profile_service.service.impl;

import com.mockmule.profile_service.dto.CreateProfileRequest;
import com.mockmule.profile_service.dto.ProfileResponse;
import com.mockmule.profile_service.dto.UpdateProfileRequest;
import com.mockmule.profile_service.entity.Profile;
import com.mockmule.profile_service.exception.DuplicateResourceException;
import com.mockmule.profile_service.exception.ResourceNotFoundException;
import com.mockmule.profile_service.mapper.ProfileMapper;
import com.mockmule.profile_service.repository.ProfileRepository;
import com.mockmule.profile_service.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapper profileMapper;

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileMapper profileMapper) {
        this.profileRepository = profileRepository;
        this.profileMapper = profileMapper;
    }

    @Override
    public ProfileResponse createProfile(Long userId , CreateProfileRequest request){

        if (profileRepository.existsByUserId(userId)){
            throw new DuplicateResourceException("Profile already exists for this userId: " + userId);
        }
        Profile profile = profileMapper.toEntity(request,userId);
        return profileMapper.toResponse(profileRepository.save(profile));

    }
    @Override
    public ProfileResponse getProfileByUserId(Long userId){

        Profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for userId: " + userId));
        return profileMapper.toResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(Long userId, UpdateProfileRequest request){

        Profile existingProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for userId: " + userId));

        profileMapper.updateEntity(existingProfile , request);

        return profileMapper.toResponse(profileRepository.save(existingProfile));
    }
}
