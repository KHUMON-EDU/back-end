package com.teamh.khumon.domain;


import com.teamh.khumon.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
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


    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "answer_id")
    private Answer answer;

//    @OneToOne(orphanRemoval = true, cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "my_answer_id")
//    private MyAnswer myAnswer;

    private String myAnswer;
    @Column(columnDefinition = "TEXT")
    private String whatWrong;
    private Boolean isCorrect;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @ToString.Exclude
    private Member member;

    @ManyToOne
    @JoinColumn(name = "learning_material_id")
    @ToString.Exclude
    private LearningMaterial learningMaterial;
}