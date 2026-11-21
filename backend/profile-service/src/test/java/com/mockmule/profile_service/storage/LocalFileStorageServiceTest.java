package com.mockmule.profile_service.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class LocalFileStorageServiceTest {

    @TempDir
    Path tempDir;

    private LocalFileStorageService createStorageService() {
        return new LocalFileStorageService(tempDir.toString());
    }

    private MockMultipartFile createTestFile() {
        return new MockMultipartFile(
                "file",
                "resume.pdf",
                "application/pdf",
                "Sample resume content".getBytes()
        );
    }

    @Test
    void shouldStoreUploadedFileSuccessfully() throws Exception {

        LocalFileStorageService storageService = createStorageService();

        MockMultipartFile file = createTestFile();

        String storageKey = storageService.storeFile(file);

        assertNotNull(storageKey);
        assertTrue(storageKey.endsWith(".pdf"));

        Path storedFile = tempDir.resolve(storageKey);

        assertTrue(Files.exists(storedFile));
        assertEquals(
                "Sample resume content",
                Files.readString(storedFile)
        );
    }

    @Test
    void shouldGenerateUniqueStorageKeys() {

        LocalFileStorageService storageService = createStorageService();

        String firstKey = storageService.storeFile(createTestFile());
        String secondKey = storageService.storeFile(createTestFile());

        assertNotEquals(firstKey, secondKey);
    }

    @Test
    void shouldLoadStoredFileSuccessfully() throws Exception {

        LocalFileStorageService storageService = createStorageService();

        String storageKey = storageService.storeFile(createTestFile());

        Resource resource = storageService.loadFile(storageKey);

        assertNotNull(resource);
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
        assertEquals(
                "Sample resume content",
                new String(resource.getInputStream().readAllBytes())
        );
    }

    @Test
    void shouldDeleteStoredFileSuccessfully() throws Exception {

        LocalFileStorageService storageService = createStorageService();

        String storageKey = storageService.storeFile(createTestFile());

        Path storedFile = tempDir.resolve(storageKey);

        assertTrue(Files.exists(storedFile));

        storageService.deleteFile(storageKey);

        assertFalse(Files.exists(storedFile));
    }

    @Test
    void shouldRejectInvalidStoragePath() {

        LocalFileStorageService storageService = createStorageService();

        assertThrows(
                RuntimeException.class,
                () -> storageService.loadFile("../outside.txt")
        );
    }
}
