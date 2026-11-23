package com.mockmule.profile_service.service.impl;

import com.mockmule.profile_service.dto.ResumeResponse;
import com.mockmule.profile_service.entity.Profile;
import com.mockmule.profile_service.entity.Resume;
import com.mockmule.profile_service.enums.ResumeParsingStatus;
import com.mockmule.profile_service.exception.ResourceNotFoundException;
import com.mockmule.profile_service.mapper.ResumeMapper;
import com.mockmule.profile_service.repository.ProfileRepository;
import com.mockmule.profile_service.repository.ResumeRepository;
import com.mockmule.profile_service.service.ResumeService;
import com.mockmule.profile_service.storage.FileStorageService;
import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final ProfileRepository profileRepository;
    private final FileStorageService fileStorageService;
    private final ResumeMapper resumeMapper;

    public ResumeServiceImpl(ResumeRepository resumeRepository,ProfileRepository profileRepository,
                             FileStorageService fileStorageService,ResumeMapper resumeMapper){
        this.resumeRepository = resumeRepository;
        this.profileRepository = profileRepository;
        this.fileStorageService = fileStorageService;
        this.resumeMapper = resumeMapper;
    }

    @Override
    @Transactional
    public ResumeResponse uploadResume(Long profileId, MultipartFile file){

        Profile profile = profileRepository.findByUserId(profileId).orElseThrow(()->
                new RuntimeException("Profile not found " + profileId));

        String storageKey = fileStorageService.storeFile(file);

        try{
            Resume resume = new Resume();
            resume.setProfile(profile);
            resume.setFileName(file.getOriginalFilename());
            resume.setFileSize(file.getSize());
            resume.setFileType("pdf");
            resume.setParsingStatus(ResumeParsingStatus.UPLOADED);
            resume.setStorageKey(storageKey);

            Resume savedResume = resumeRepository.save(resume);

            return resumeMapper.toResponse(savedResume);

        }catch(RuntimeException ex){
            fileStorageService.deleteFile(storageKey);
            throw ex;
        }

    }

    @Override
    public List<ResumeResponse> getResumesByProfileId(Long profileId){

        if(!profileRepository.existsById(profileId)){
            throw new RuntimeException("Profile doesn't exists with id "+ profileId);
        }
        List<Resume> resumesByProfileId = resumeRepository.findByProfileIdAndDeleteFalse(profileId);

        return resumesByProfileId.stream().map(resumeMapper::toResponse).toList();

    }

    @Override
    public Resource downloadResume(Long profileId, Long resumeId){

        if(!profileRepository.existsById(profileId)){
            throw new RuntimeException("Profile doesn't exists with id "+ profileId);
        }
        Resume resumes = resumeRepository.findByProfileIdAndIdAndDeleteFalse(profileId, resumeId)
                .orElseThrow(()-> new RuntimeException("Resume not found with ResumeId "+ resumeId));

        return fileStorageService.loadFile(resumes.getStorageKey());
    }


    @Override
    @Transactional
    public void deleteResume(Long profileId, Long resumeId){

        if(!profileRepository.existsById(profileId)){
            throw new ResourceNotFoundException("Profile doesn't exists with id"+ profileId);
        }

        Resume resume = resumeRepository.findByProfileIdAndIdAndDeleteFalse(profileId, resumeId)
                .orElseThrow(()->new ResourceNotFoundException("Resume Doesn't Exists with " +
                        "profileId : "+profileId+ " and resumeId: " + resumeId));

        resume.setDelete(true);
        resume.setDeletedAt(LocalDateTime.now());

        resumeRepository.save(resume);
    }
}
