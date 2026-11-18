package com.mockmule.profile_service.repository;

import com.mockmule.profile_service.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume,Long > {

    List<Resume> findByProfileId(Long profileId);
    Optional<Resume> findByIdAndProfileId(Long resumeId, Long profileId);
    boolean existsByStorageKey(String storageKey);
}
