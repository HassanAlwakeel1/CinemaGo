package com.CinemaGo.service.impl;

import com.CinemaGo.service.CloudinaryImageService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private static final Logger logger = Logger.getLogger(CloudinaryImageServiceImpl.class.getName());


    private final Cloudinary cloudinary;

    public CloudinaryImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Map<String, Object> upload(byte[] fileBytes) {
        logger.info("Starting image upload from byte array...");
        try {
            Map<String, Object> result = cloudinary.uploader().upload(fileBytes, ObjectUtils.emptyMap());
            logger.info("Image uploaded successfully. Public ID: " + result.get("public_id"));
            return result;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Image upload from byte array failed", e);
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public Map<String, Object> upload(MultipartFile file) {
        logger.info("Starting image upload from MultipartFile: " + file.getOriginalFilename());
        try {
            Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            logger.info("Image uploaded successfully. Public ID: " + result.get("public_id"));
            return result;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Image upload from MultipartFile failed: " + file.getOriginalFilename(), e);
            throw new RuntimeException("Image upload failed", e);
        }
    }
}
