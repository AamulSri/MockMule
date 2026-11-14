package com.mockmule.profile_service.repository;

import com.mockmule.profile_service.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Long> {

    Optional<Profile> findByUserId(Long userId);
    boolean existsByUserId(Long userId);

}
