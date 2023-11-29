package com.teamh.khumon.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PriorityLearningMaterialContent {
    private Long id;
    private String title;
    private String content;
    private Boolean isPriority;
    private String type;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
}
