package com.mockmule.profile_service.controller;


import com.mockmule.profile_service.dto.ApiResponse;
import com.mockmule.profile_service.dto.ResumeResponse;
import com.mockmule.profile_service.service.ResumeService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1/profile/{profileId}")
@RestController
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }


    @GetMapping("/getResumes")
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getResumesByProfileId(@PathVariable Long profileId){

        List<ResumeResponse> resumeResponse = resumeService.getResumesByProfileId(profileId);

        ApiResponse<List<ResumeResponse>> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Resumes Retrived Successfully",
                resumeResponse,
                null
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/resumes")
    public ResponseEntity<ApiResponse<ResumeResponse>> uploadResume(
            @PathVariable Long profileId , @RequestParam("file") MultipartFile file){

        ResumeResponse resumeResponse = resumeService.uploadResume(profileId, file);

        ApiResponse<ResumeResponse> apiResponse = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Resume Uploaded Successfully",
                resumeResponse,
                null
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/download/{resumeId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long profileId ,
                                                                @PathVariable Long resumeId){

        Resource resource = resumeService.downloadResume(profileId, resumeId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }

    @DeleteMapping("/delete/{resumeId}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long profileId,
                                                          @PathVariable Long resumeId){

        resumeService.deleteResume(profileId,resumeId);
        ApiResponse<Void> response = new ApiResponse<>(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Deleted Resume Successfully",
                null,
                null
        );
        return ResponseEntity.ok(response);
    }
}
