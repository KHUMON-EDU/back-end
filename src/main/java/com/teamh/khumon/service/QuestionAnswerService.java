package com.teamh.khumon.service;

import com.teamh.khumon.domain.*;
import com.teamh.khumon.dto.AIResponse;
import com.teamh.khumon.dto.MyAnswerAIResponse;
import com.teamh.khumon.dto.MyAnswerRequest;
import com.teamh.khumon.dto.Problem;
import com.teamh.khumon.repository.AnswerRepository;

import com.teamh.khumon.repository.QuestionRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuestionAnswerService {

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

//    private final MyAnswerRepository myAnswerRepository;

    @Transactional
    public List<Question> saveQuestionAndAnswer(AIResponse aiResponse, Member member, LearningMaterial learningMaterial){
        List<Problem> problems = aiResponse.getProblems();
        List<Question> questions = new ArrayList<>();
        for(Problem problem : problems){
            Question question = new Question();
            question.setMember(member);
            question.setLearningMaterial(learningMaterial);
            question.setContent(problem.getQuestion());
            questionRepository.save(question);
            Answer answer = new Answer();
            answer.setAnswer(problem.getAnswer());
            answer.setQuestion(question);
            answerRepository.save(answer);
            question.setAnswer(answer);
            questions.add(question);
        }
        return questions;
    }

    @Transactional
    void deleteQuestionAndAnswer(LearningMaterial learningMaterial){
        List<Question> questionList = learningMaterial.getQuestions();
        questionRepository.deleteAll(questionList);
    }


    @Transactional
    public Long postMyAnswer(Long questionId, MyAnswerRequest myAnswerRequest, MyAnswerAIResponse myAnswerAIResponse) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        question.setMyAnswer(myAnswerRequest.getContent());
        question.setIsCorrect(myAnswerAIResponse.getCorrect());
        question.setWhatWrong(myAnswerAIResponse.getAssessment());
        questionRepository.save(question);
        return questionId;
    }
}
