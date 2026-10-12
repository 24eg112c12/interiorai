package com.interiorai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${interiorai.upload.dir:uploads/rooms}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve uploaded room photos
        File uploadFolder = new File(uploadDir);
        String uploadAbsolutePath = uploadFolder.getAbsolutePath().replace("\\", "/");
        if (!uploadAbsolutePath.endsWith("/")) {
            uploadAbsolutePath += "/";
        }

        registry.addResourceHandler("/uploads/**", "/api/uploads/**")
                .addResourceLocations("file:" + uploadAbsolutePath, "file:uploads/rooms/");

        // Also serve frontend from local directory if backend and frontend are in peer folders
        String frontendPath = Paths.get("..", "frontend").toAbsolutePath().normalize().toString().replace("\\", "/") + "/";
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "file:" + frontendPath);
    }
}
