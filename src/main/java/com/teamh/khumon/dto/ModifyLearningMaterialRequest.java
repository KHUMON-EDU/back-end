package com.teamh.khumon.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ModifyLearningMaterialRequest {
    private String title;

    private String content;
}
