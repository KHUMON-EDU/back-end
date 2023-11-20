package com.teamh.khumon.dto;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LearningMaterialContent {
    private Long id;
    private String title;
    private String content;
    private String type;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

}
