package com.quiz.g4.dto;

public class AnswerOptionForm {

    private String content;
    private Boolean isCorrect;

    public AnswerOptionForm(String content, Boolean isCorrect) {
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    // Getters and Setters
}
