
package com.mockmule.profile_service.mapper;

import com.mockmule.profile_service.dto.CreateProfileRequest;
import com.mockmule.profile_service.dto.ProfileResponse;
import com.mockmule.profile_service.dto.UpdateProfileRequest;
import com.mockmule.profile_service.entity.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public Profile toEntity(CreateProfileRequest request, Long userId) {

        Profile profile = new Profile();

        profile.setUserId(userId);
        profile.setFullName(request.fullName());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setLocation(request.location());
        profile.setHeadline(request.headline());
        profile.setSummary(request.summary());

        return profile;
    }

    public void updateEntity(
            Profile profile,
            UpdateProfileRequest request) {

        profile.setFullName(request.fullName());
        profile.setPhoneNumber(request.phoneNumber());
        profile.setLocation(request.location());
        profile.setHeadline(request.headline());
        profile.setSummary(request.summary());
    }

    public ProfileResponse toResponse(Profile profile) {

        return new ProfileResponse(
                profile.getId(),
                profile.getUserId(),
                profile.getFullName(),
                profile.getPhoneNumber(),
                profile.getLocation(),
                profile.getHeadline(),
                profile.getSummary(),
                profile.getCreatedAt(),
                profile.getUpdatedAt()
        );
    }
}
