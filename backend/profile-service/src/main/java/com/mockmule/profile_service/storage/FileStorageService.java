package com.mockmule.profile_service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file);
    Resource loadFile(String storageKey);
    void deleteFile(String storageKey);
}
