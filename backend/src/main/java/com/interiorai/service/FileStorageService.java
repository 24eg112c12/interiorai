package com.interiorai.service;

import com.interiorai.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "webp");
    private static final List<String> ALLOWED_MIME_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp");
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB

    public FileStorageService(@Value("${interiorai.upload.dir:uploads/rooms}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create directory for uploaded files.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Uploaded room photo is empty or missing.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new BadRequestException("File size exceeds 10 MB limit.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            throw new BadRequestException("Invalid file type (" + contentType + "). Supported formats: JPEG, PNG, WebP.");
        }

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
            extension = originalFilename.substring(dotIndex + 1).toLowerCase();
        }

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BadRequestException("Unsupported file extension ." + extension + ". Supported formats: JPG, JPEG, PNG, WEBP.");
        }

        // Generate safe unique filename to prevent overwrites and directory traversal
        String uniqueFileName = "room_" + UUID.randomUUID() + "." + extension;

        try {
            Path targetLocation = this.fileStorageLocation.resolve(uniqueFileName);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }
            // Return public accessible URL
            return "/uploads/" + uniqueFileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store file " + uniqueFileName + ". Please try again.", ex);
        }
    }

    public void deleteFile(String fileUrl) {
        if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
            String fileName = fileUrl.substring("/uploads/".length());
            try {
                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
                if (filePath.startsWith(this.fileStorageLocation)) {
                    Files.deleteIfExists(filePath);
                }
            } catch (IOException ignored) {
            }
        }
    }
}
