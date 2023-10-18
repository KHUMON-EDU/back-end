package com.teamh.khumon.domain;


import com.teamh.khumon.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;


    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    @ManyToOne()
    @JoinColumn(name = "learning_material_id")
    @ToString.Exclude
    private LearningMaterial learningMaterial;
}