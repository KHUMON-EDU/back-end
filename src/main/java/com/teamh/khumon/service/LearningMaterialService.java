package com.teamh.khumon.service;


import com.teamh.khumon.domain.LearningMaterial;
import com.teamh.khumon.domain.MediaFileType;
import com.teamh.khumon.domain.Member;
import com.teamh.khumon.domain.Question;
import com.teamh.khumon.dto.*;
import com.teamh.khumon.repository.LearningMaterialRepository;
import com.teamh.khumon.repository.MemberRepository;
import com.teamh.khumon.util.AmazonS3Util;
import com.teamh.khumon.util.MediaUtil;
import com.teamh.khumon.util.ObjectToDtoUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            log.info("저장된 ID : " + id);
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


    public ResponseEntity<?> getLearningMaterials(Principal principal, Pageable pageable, String search) {
        Member member = memberRepository.findByUsername(principal.getName()).orElseThrow();
        Page<LearningMaterial> learningMaterials = learningMaterialRepository.findByMemberIdAndTitleContainingOrContentContaining(member.getId(), search, search, pageable);
        List<LearningMaterialContent> learningMaterialContents = learningMaterials.getContent().stream().map(learningMaterial -> LearningMaterialContent.builder()
                .title(learningMaterial.getTitle())
                .content(learningMaterial.getContent())
                .createAt(learningMaterial.getCreatedAt())
                .modifiedAt(learningMaterial.getUpdateAt())
                .build()).toList();

        OffsetPagination offsetPagination = OffsetPagination.builder()
                .content(learningMaterialContents)
                .isEmpty(learningMaterials.isEmpty())
                .isFirst(learningMaterials.isFirst())
                .isLast(learningMaterials.isLast())
                .numberOfElements(learningMaterials.getNumberOfElements())
                .pageNumber(learningMaterials.getNumber())
                .size(learningMaterials.getSize())
                .totalElements(learningMaterials.getTotalElements())
                .totalPages(learningMaterials.getTotalPages())
                .build();


        return new ResponseEntity<OffsetPagination>(offsetPagination, HttpStatus.OK);
    }



    @Transactional
    public ResponseEntity<?> modifyLearningMaterial(Long id, Principal principal, ModifyLearningMaterialRequest modifyLearningMaterialRequest) {
        LearningMaterial learningMaterial = learningMaterialRepository.findById(id).orElseThrow();
        if(!principal.getName().equals(learningMaterial.getMember().getUsername())){
            throw new RuntimeException("작성자가 아님");
        }
        if(modifyLearningMaterialRequest.getTitle() != null){
            learningMaterial.setTitle(modifyLearningMaterialRequest.getTitle());
        }
        if(modifyLearningMaterialRequest.getContent() != null){
            learningMaterial.setContent(modifyLearningMaterialRequest.getContent());
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<?> delete(Principal principal, Long id) {
        LearningMaterial learningMaterial = learningMaterialRepository.findById(id).orElseThrow();
        if(!principal.getName().equals(learningMaterial.getMember().getUsername())){
            throw new RuntimeException("작성자가 아님");
        }
        amazonS3Util.deleteFile(learningMaterial.getFileURL());
        questionAnswerService.deleteQuestionAndAnswer(learningMaterial);
        learningMaterialRepository.deleteById(id);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

}