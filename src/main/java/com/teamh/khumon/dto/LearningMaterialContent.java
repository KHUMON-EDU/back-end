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

    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

}
