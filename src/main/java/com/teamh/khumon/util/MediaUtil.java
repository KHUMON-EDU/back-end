package com.teamh.khumon.util;

import com.teamh.khumon.domain.MediaFileType;
import com.teamh.khumon.domain.Question;
import com.teamh.khumon.dto.MyAnswerRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.PathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class MediaUtil {

    private final String MAIN_DIR_NAME = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static";

    private final String SUB_DIR_NAME =  File.separator + "material";


    private String storeMaterial(MultipartFile media) throws Exception {
        log.info(MAIN_DIR_NAME);
        log.info(SUB_DIR_NAME);
        try {
            File folder = new File(MAIN_DIR_NAME + SUB_DIR_NAME);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String fileName = media.getOriginalFilename();
            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String generateFileName;
            generateFileName = UUID.randomUUID().toString() + "." + extension;
            String mediaURL =  File.separator + generateFileName;
            String destinationPath = folder.getPath() + mediaURL;
            File destination = new File(destinationPath);
            media.transferTo(destination);


            return destinationPath;
        } catch (Exception e) {
            throw new Exception("강의자료 형식이 아님");
        }
    }

    public MediaFileType findMediaType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        log.info("extension : " + extension);
        if (extension.equals("txt")) {
            log.info("txt");
            return MediaFileType.TEXT;
        } else if(extension.equals("pdf")){
            log.info("pdf");
            return MediaFileType.PDF;
        } else if(extension.equals("mp4") || extension.equals("mpeg") || extension.equals("mov") || extension.equals("mkv") || extension.equals("avi") || extension.equals("wmv")){
            log.info("video");
            return MediaFileType.VIDEO;
        }else{
            throw  new RuntimeException("파일 포맷을 지원하지 않음");
        }
    }


    //Text일 때, String으로 컨버팅
    public String readFileToString(MultipartFile multipartFile) throws Exception {
        String destinationPathString = storeMaterial(multipartFile);
        File file = new File(destinationPathString);
        String result = FileUtils.readFileToString(file, "UTF-8");
        deleteMediaFile(destinationPathString);
        return result;
    }


    public String postToLLMforText(String text) throws JSONException {
        String postUrl = "http://facerain-dev.iptime.org:5000/api/v1/generation/text";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", text);

        HttpEntity<String> requestHttpEntity =
                new HttpEntity<>(jsonObject.toString(), httpHeaders);

        String response = restTemplate.postForObject(postUrl, requestHttpEntity, String.class);
        log.info(response);
        return response;
    }

    public String postToLLMforPDF(MultipartFile multipartFile) throws Exception {
        String postUrl = "http://facerain-dev.iptime.org:5000/api/v1/generation/pdf";
        RestTemplate restTemplate = new RestTemplate();
        String destinationPathString = storeMaterial(multipartFile);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            Path path = Paths.get(destinationPathString);
            body.add("upload_file", new PathResource(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpEntity<MultiValueMap<String, Object>> requestHttpEntity =
                new HttpEntity<>(body, httpHeaders);

        String response = restTemplate.postForObject(postUrl, requestHttpEntity, String.class);
        log.info(response);
        deleteMediaFile(destinationPathString);
        return response;
    }

    public String postToLLMforVideo(MultipartFile multipartFile) throws Exception {
        String postUrl = "http://facerain-dev.iptime.org:5000/api/v1/generation/video";
        RestTemplate restTemplate = new RestTemplate();
        String destinationPathString = storeMaterial(multipartFile);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        try {
            Path path = Paths.get(destinationPathString);
            body.add("upload_file", new PathResource(path));

        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpEntity<MultiValueMap<String, Object>> requestHttpEntity =
                new HttpEntity<>(body, httpHeaders);
        String response = null;
        try {
            response = restTemplate.postForObject(postUrl, requestHttpEntity, String.class);
            log.info(response);
            deleteMediaFile(destinationPathString);

        }catch (Exception E){
            log.info(E.getMessage());
        }
        return response;
    }


    public String postToLLMforUserAnswer(MyAnswerRequest myAnswerRequest, String question) throws Exception {
        String postUrl = "http://facerain-dev.iptime.org:5000/api/v1/generation/assessment";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("question", question);
        jsonObject.put("answer", myAnswerRequest.getContent());

//question : STring
//answer: STring
        HttpEntity<String> requestHttpEntity =
                new HttpEntity<>(jsonObject.toString(), httpHeaders);
        String response = restTemplate.postForObject(postUrl, requestHttpEntity, String.class);
        log.info(response);
        return response;
    }



    public void deleteMediaFile(String filePath) {
        File savedFile = new File(filePath);
        if (savedFile.exists()) {
            if (savedFile.delete()) {
                log.info("파일삭제 성공. filename : {}", savedFile);
            } else {
                log.info("파일삭제 실패. filename : {}", savedFile);
            }
        } else {
            log.info("파일이 존재하지 않습니다. filename : {}", savedFile);
        }
    }




}
