package com.teamh.khumon.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionInformation {
    private Long id;
    private String content;
    private String myAnswer;
    private Boolean isCorrect;
    private String whatWrong;
    private String answer;
}
