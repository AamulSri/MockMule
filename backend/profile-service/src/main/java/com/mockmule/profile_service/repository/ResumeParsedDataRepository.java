package com.mockmule.profile_service.repository;

import com.mockmule.profile_service.entity.ResumeParsedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeParsedDataRepository extends JpaRepository<ResumeParsedData, Long> {

    Optional<ResumeParsedData> findByResumeId(Long resumeId);
}