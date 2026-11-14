package com.mockmule.profile_service.service;

import com.mockmule.profile_service.entity.Profile;

import java.util.Optional;

public interface ProfileService {

    Profile createProfile(Profile profile);
    Optional<Profile> getProfileByUserId(Long userId);
    Profile updateProfile(Long userId, Profile profile);
}
