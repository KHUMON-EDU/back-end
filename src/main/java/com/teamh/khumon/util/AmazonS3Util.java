package com.teamh.khumon.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Util {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;


    public String uploadS3Object(String username, MultipartFile object, Long learningId) throws Exception {
            String originalName = object.getOriginalFilename();
            String extension = Objects.requireNonNull(originalName).substring(originalName.lastIndexOf(".") + 1);
            String generateFileName = UUID.randomUUID() + "." + extension;
            log.info(generateFileName);


            String filename = username + File.separator +  learningId + File.separator + generateFileName;
            log.info(filename);
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(object.getContentType());
            objectMetadata.setContentEncoding("UTF-8");
            objectMetadata.setContentLength(object.getInputStream().available());

            amazonS3Client.putObject(bucket, filename, object.getInputStream(), objectMetadata);
            log.info(amazonS3Client.getUrl(bucket, filename).toString());
            return amazonS3Client.getUrl(bucket, filename).toString();

    }


}
