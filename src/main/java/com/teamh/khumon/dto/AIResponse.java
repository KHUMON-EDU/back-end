package com.teamh.khumon.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AIResponse {
    private String summary;
    private List<Problem> problems;
}
