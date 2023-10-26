package com.teamh.khumon.dto;


import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Problem {

    private Long problem_no;
    private String answer;
    private String question;

}
