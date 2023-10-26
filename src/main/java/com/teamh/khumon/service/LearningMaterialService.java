package com.teamh.khumon.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.teamh.khumon.domain.LearningMaterial;
import com.teamh.khumon.domain.MediaFileType;
import com.teamh.khumon.domain.Member;
import com.teamh.khumon.domain.Question;
import com.teamh.khumon.dto.AIResponse;
import com.teamh.khumon.dto.LearningMaterialResponse;
import com.teamh.khumon.dto.LearningRequest;
import com.teamh.khumon.dto.QuestionInformation;
import com.teamh.khumon.repository.LearningMaterialRepository;
import com.teamh.khumon.repository.MemberRepository;
import com.teamh.khumon.util.AmazonS3Util;
import com.teamh.khumon.util.MediaUtil;
import com.teamh.khumon.util.ObjectToDtoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LearningMaterialService {


    private final MemberRepository memberRepository;

    private final LearningMaterialRepository learningMaterialRepository;

    private final QuestionAnswerService questionAnswerService;

    private final MediaUtil mediaUtil;

    private final AmazonS3Util amazonS3Util;


    @Transactional
    public ResponseEntity<?> createLearningMaterial(Principal principal, MultipartFile multipartFile, String data) throws IOException {
        Member member = memberRepository.findByUsername(principal.getName()).orElseThrow();
        log.info(multipartFile.getOriginalFilename());
        try {
            LearningRequest learningRequest = (LearningRequest) new ObjectToDtoUtil().jsonStrToObj(data, LearningRequest.class);
            LearningMaterial learningMaterial = LearningMaterial.builder()
                    .title(learningRequest.getTitle())
                    .content(learningRequest.getContent())
                    .member(member)
                    .build();
            Long id = learningMaterialRepository.save(learningMaterial).getId();
            String uploadedFileUrl = amazonS3Util.uploadS3Object(principal.getName(), multipartFile, id);
            learningMaterial.setFileName(multipartFile.getOriginalFilename());
            learningMaterial.setFileURL(uploadedFileUrl);
            learningMaterial.setMediaFileType(mediaUtil.findMediaType(multipartFile.getOriginalFilename()));
            String aiServerResponse = null;
            if(learningMaterial.getMediaFileType().getFileType().equals(MediaFileType.TEXT.getFileType())){
                //리팩토링 필요
                String content = mediaUtil.readFileToString(multipartFile);
                aiServerResponse  = mediaUtil.postToLLMforText(content);
                log.info(aiServerResponse);
            }  else if(learningMaterial.getMediaFileType().getFileType().equals(MediaFileType.PDF.getFileType())){
                aiServerResponse = mediaUtil.postToLLMforPDF(multipartFile);
                log.info(aiServerResponse);
            }else if (learningMaterial.getMediaFileType().getFileType().equals(MediaFileType.VIDEO.getFileType())){
                aiServerResponse = mediaUtil.postToLLMforVideo(multipartFile);
                log.info(aiServerResponse);
            }

            AIResponse aiResponse= (AIResponse) new ObjectToDtoUtil().jsonStrToObj(aiServerResponse, AIResponse.class);
            log.info(aiResponse.toString());

            learningMaterial.setSummary(aiResponse.getSummary());
            List<Question> questions = questionAnswerService.saveQuestionAndAnswer(aiResponse, member, learningMaterial);

            learningMaterial.setQuestions(questions);

            Map<String, Long> response = new HashMap<>();
            response.put("id", id);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> getLearningMaterial(Long id, Principal principal) {
        LearningMaterial learningMaterial = learningMaterialRepository.findById(id).orElseThrow();
        if(!principal.getName().equals(learningMaterial.getMember().getUsername())){
            throw new RuntimeException("작성자가 아님");
        }

        List<QuestionInformation> responses = learningMaterial.getQuestions().stream().map(question-> QuestionInformation.builder()
                .id(question.getId())
                .content(question.getContent())
                .answer(question.getAnswer().getAnswer())
                .build()).toList();



        LearningMaterialResponse learningMaterialResponse = LearningMaterialResponse.builder()
                .id(learningMaterial.getId())
                .title(learningMaterial.getTitle())
                .content(learningMaterial.getContent())
                .summary(learningMaterial.getSummary())
                .mediaFileType(learningMaterial.getMediaFileType().getFileType())
                .mediaOriginalName(learningMaterial.getFileName())
                .mediaURL(learningMaterial.getFileURL())
                .createdDateTime(learningMaterial.getCreatedAt())
                .modifiedDateTime(learningMaterial.getUpdateAt())
                .questionInformations(responses).build();

        return new ResponseEntity<>(learningMaterialResponse, HttpStatus.OK);
    }


}