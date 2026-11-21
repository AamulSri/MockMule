package com.mockmule.profile_service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


@Service
public class LocalFileStorageService implements FileStorageService{

    private final Path storageLocation;

    public LocalFileStorageService(@Value("${file.storage.location:uploads/resumes}")String location) {
        this.storageLocation = Paths.get(location).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.storageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not initialise file storage", ex);
        }
    }

    @Override
    public String storeFile(MultipartFile file){
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String storageKey = UUID.randomUUID() + (extension!=null? "."+extension : "");

        try{
            Path targetLocation = storageLocation.resolve(storageKey).normalize();

            if(!targetLocation.startsWith(storageLocation)){
                throw new RuntimeException("Invalid Storage Path");
            }

            Files.copy(file.getInputStream(), targetLocation,StandardCopyOption.REPLACE_EXISTING);

            return storageKey;
        }
        catch( IOException ex){
            throw  new RuntimeException("Couldn't store file", ex);
        }
    }


    @Override
    public Resource loadFile(String storageKey) {
        try {
            Path filePath = storageLocation.resolve(storageKey).normalize();

            if (!filePath.startsWith(storageLocation)
                    || filePath.equals(storageLocation)) {
                throw new RuntimeException("Invalid Storage Path");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException("File not found: " + storageKey);

        } catch (IOException ex) {
            throw new RuntimeException("Could not load the file", ex);
        }
    }

    @Override
    public void deleteFile(String storageKey) {
        try {
            Path filePath = storageLocation.resolve(storageKey).normalize();

            if (!filePath.startsWith(storageLocation)) {
                throw new RuntimeException("Invalid Storage Path");
            }
            Files.deleteIfExists(filePath);

        } catch(IOException ex){
            throw new RuntimeException("Can't delete file", ex);
        }
    }

}
