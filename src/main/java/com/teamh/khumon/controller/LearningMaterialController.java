package com.teamh.khumon.controller;

import com.teamh.khumon.dto.ModifyLearningMaterialRequest;
import com.teamh.khumon.dto.MyAnswerRequest;
import com.teamh.khumon.service.LearningMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class LearningMaterialController {

    private final LearningMaterialService learningMaterialService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value ="/learning-material", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> createLearningMaterial(@RequestPart(value = "media", required= true) MultipartFile multipartFile,
                                                    @RequestPart(value = "data", required = true) String data,
                                                    Principal principal) throws IOException {
        return learningMaterialService.createLearningMaterial(principal, multipartFile, data);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/learning-material/{learning-material-id}")
    public ResponseEntity<?> getLearningMaterial(@PathVariable(name = "learning-material-id") Long id, Principal principal){
        return learningMaterialService.getLearningMaterial(id, principal);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/learning-materials")
    public ResponseEntity<?> getLearningMaterials(Principal principal, @PageableDefault(sort = "id", direction = Sort.Direction.DESC, size = 10) Pageable pageable, @RequestParam(required = false, defaultValue = "", value = "search") String search){
        return learningMaterialService.getLearningMaterials(principal, pageable, search);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/learning-material/{learning-material-id}")
    public ResponseEntity<?> modifyLearningMaterials(Principal principal, @PathVariable(name = "learning-material-id", required = true) Long id, @RequestBody ModifyLearningMaterialRequest modifyLearningMaterialRequest){
        return learningMaterialService.modifyLearningMaterial(id, principal,modifyLearningMaterialRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/learning-material/{learning-material-id}")
    public ResponseEntity<?> deleteLearning(Principal principal,@PathVariable(name ="learning-material-id")Long id){
        return learningMaterialService.delete(principal, id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/learning-material/{learning-material-id}/question/{question-id}")
    public ResponseEntity<?> postMyAnswer(Principal principal, @PathVariable(name="learning-material-id") Long learningMaterialId, @PathVariable(name="question-id") Long myanswerId, @RequestBody MyAnswerRequest myAnswerRequest) throws Exception {
        log.info("엔드포인트 지남");
        return learningMaterialService.postMyAnswer(principal, learningMaterialId, myanswerId, myAnswerRequest);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/learning-material/priority/{learning-material-id}")
    public ResponseEntity<?> setPriority(@PathVariable(name = "learning-material-id")Long id, Principal principal){
        return learningMaterialService.setPriority(principal, id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/learning-material/unpriority/{learning-material-id}")
    public ResponseEntity<?> unsetPriority(@PathVariable(name = "learning-material-id")Long id, Principal principal){
        return learningMaterialService.unsetPriority(principal, id);
    }
}

