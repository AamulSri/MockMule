package com.mockmule.profile_service.service.impl;

import com.mockmule.profile_service.entity.Profile;
import com.mockmule.profile_service.repository.ProfileRepository;
import com.mockmule.profile_service.service.ProfileService;

import java.util.Optional;

public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile createProfile(Profile profile){

        if (profileRepository.existsByUserId(profile.getUserId())) {
            throw new IllegalStateException("Profile already exists for this userId: " + profile.getUserId());
        }
        return profileRepository.save(profile);
    }
    @Override
    public  Optional<Profile> getProfileByUserId(Long userId){

        return profileRepository.findByUserId(userId);

    }
    @Override
    public Profile updateProfile(Long userId, Profile profile){

        Profile existingProfile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalStateException("Profile not found for userId: " + userId));

        existingProfile.setFullName(profile.getFullName());
        existingProfile.setLocation(profile.getLocation());
        existingProfile.setPhoneNumber(profile.getPhoneNumber());
        existingProfile.setHeadline(profile.getHeadline());
        existingProfile.setSummary(profile.getSummary());

        return profileRepository.save(existingProfile);

    }
}
