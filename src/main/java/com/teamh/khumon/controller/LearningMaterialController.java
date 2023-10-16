package com.teamh.khumon.controller;

import com.teamh.khumon.service.LearningMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LearningMaterialController {

    private final LearningMaterialService learningMaterialService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value ="/learning-material", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createRoom(@RequestPart(value = "media", required= true) MultipartFile multipartFile,
                                        @RequestPart(value = "data", required = true) String data,
                                        Principal principal) throws IOException {
        return learningMaterialService.createLearningMaterial(principal, multipartFile, data);
    }


}

