package com.teamh.khumon.dto;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyAnswerAIResponse {
    private String assessment;
    private Boolean correct;
}


//question : STring
//answer: STring
