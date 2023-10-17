package com.teamh.khumon.util;

import com.teamh.khumon.domain.MediaFileType;
import io.lettuce.core.dynamic.annotation.CommandNaming;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
@Component
public class MediaUtil {

    private final String MAIN_DIR_NAME = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources";

    private final String SUB_DIR_NAME = File.separator + "static";

    public String uploadMaterial(MultipartFile media) throws Exception {
        log.info(MAIN_DIR_NAME);
        log.info(SUB_DIR_NAME);
        try {
            File folder = new File(MAIN_DIR_NAME + SUB_DIR_NAME +  File.separator + "material");

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


            return SUB_DIR_NAME + mediaURL;
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
            log.info("etc");
            return MediaFileType.ETC;
        }
    }


}
