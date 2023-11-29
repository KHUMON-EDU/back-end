package com.teamh.khumon.dto;


import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OffsetPagination {
    private List<LearningMaterialContent> content;
    private List<PriorityLearningMaterialContent> priorityLearningMaterialContents;
    private Long totalElements;
    private Integer totalPages;
    private Integer size;
    private Integer pageNumber;
    private Integer numberOfElements;
    private Boolean isFirst;
    private Boolean isLast;
    private Boolean isEmpty;
}
