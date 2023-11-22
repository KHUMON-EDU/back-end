package com.teamh.khumon.domain;


import com.teamh.khumon.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString(callSuper = true)
@Table(name = "learning_material")
public class LearningMaterial extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(columnDefinition = "TEXT")
    private String textData;

    @Enumerated(EnumType.STRING)
    private MediaFileType mediaFileType;

    @Column
    private String fileName;

    @Column(unique = true)
    private String fileURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    @OneToMany(mappedBy = "learningMaterial", fetch = FetchType.LAZY)
    @ToString.Exclude
    List<Question> questions = new ArrayList<>();

}