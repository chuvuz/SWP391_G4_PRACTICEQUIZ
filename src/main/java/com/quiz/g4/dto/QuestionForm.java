package com.quiz.g4.dto;


import java.util.List;

public class QuestionForm {

    private String questionContent;
    private String questionType;
    private List<AnswerOptionForm> answerOptions;

    public QuestionForm(String questionContent, String questionType, List<AnswerOptionForm> answerOptions) {
        this.questionContent = questionContent;
        this.questionType = questionType;
        this.answerOptions = answerOptions;
    }

    public List<AnswerOptionForm> getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(List<AnswerOptionForm> answerOptions) {
        this.answerOptions = answerOptions;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    // Getters and Setters
}

