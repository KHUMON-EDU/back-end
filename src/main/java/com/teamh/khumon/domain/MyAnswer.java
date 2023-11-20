//package com.teamh.khumon.domain;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//
//@Entity
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class MyAnswer {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column
//    private String myAnswer;
//
//    @Column
//    private Boolean isCorrect;
//
//    @OneToOne(mappedBy = "answer")
//    private Question question;
//}
