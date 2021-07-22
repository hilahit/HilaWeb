package com.example.parkinson.model.question_models;

import com.example.parkinson.model.enums.EQuestionType;

public class OpenQuestion extends Question {
    private String answer;

    public OpenQuestion() {
        super("", null);
    }

    public OpenQuestion(String title, EQuestionType type, String answer) {
        super(title, type);
        this.answer = answer;
    }
    public OpenQuestion(String title, EQuestionType type) {
        super(title, type);
        this.answer = null;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
