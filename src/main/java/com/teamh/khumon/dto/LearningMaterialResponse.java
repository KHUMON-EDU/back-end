package com.teamh.khumon.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LearningMaterialResponse {
    private Long id;
    private String title;
    private String content;
    private String mediaURL;
    private String mediaOriginalName;
    private String mediaFileType;
    private LocalDateTime createdDateTime;
    private LocalDateTime modifiedDateTime;
    private String script;
    private String summary;
    private List<QuestionInformation> questionInformations;

}
