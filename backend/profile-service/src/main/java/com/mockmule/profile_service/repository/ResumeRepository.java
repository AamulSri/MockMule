package com.mockmule.profile_service.repository;

import com.mockmule.profile_service.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume,Long > {


    List<Resume> findByProfileIdAndDeleteFalse(Long profileId);

    Optional<Resume> findByProfileIdAndIdAndDeleteFalse(
            Long profileId,
            Long resumeId
    );

    boolean existsByStorageKey(String storageKey);
}
