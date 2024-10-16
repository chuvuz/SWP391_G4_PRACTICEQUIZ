package com.quiz.g4.dto;


import java.util.List;

public class QuestionForm {

    private String questionContent;
    private String questionType;

    public QuestionForm(String questionContent, String questionType) {
        this.questionContent = questionContent;
        this.questionType = questionType;
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

