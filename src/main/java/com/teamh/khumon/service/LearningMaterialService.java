package com.teamh.khumon.service;


import com.teamh.khumon.domain.LearningMaterial;
import com.teamh.khumon.domain.Member;
import com.teamh.khumon.dto.LearningRequest;
import com.teamh.khumon.repository.LearningMaterialRepository;
import com.teamh.khumon.repository.MemberRepository;
import com.teamh.khumon.util.MediaUtil;
import com.teamh.khumon.util.ObjectToDtoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class LearningMaterialService {


    private final MemberRepository memberRepository;

    private final LearningMaterialRepository learningMaterialRepository;

    private final MediaUtil mediaUtil;


    @Transactional
    public ResponseEntity<?> createLearningMaterial(Principal principal, MultipartFile multipartFile, String data) {
        Member member = memberRepository.findByUsername(principal.getName()).orElseThrow();
        log.info(multipartFile.getOriginalFilename());
        try {
            LearningRequest learningRequest = (LearningRequest) new ObjectToDtoUtil().jsonStrToObj(data, LearningRequest.class);
            LearningMaterial learningMaterial = LearningMaterial.builder()
                    .title(learningRequest.getTitle())
                    .content(learningRequest.getContent())
                    .member(member)
                    .build();
            String uploadedFileUrl = mediaUtil.uploadMaterial(multipartFile);
            learningMaterial.setFileName(multipartFile.getOriginalFilename());
            learningMaterial.setFileURL(uploadedFileUrl);
            learningMaterial.setMediaFileType(mediaUtil.findMediaType(multipartFile.getOriginalFilename()));
            Long id = learningMaterialRepository.save(learningMaterial).getId();
            Map<String, Long> response = new HashMap<>();
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}