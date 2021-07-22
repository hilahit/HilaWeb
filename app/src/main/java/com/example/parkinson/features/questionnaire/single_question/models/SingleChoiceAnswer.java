package com.example.parkinson.features.questionnaire.single_question.models;

public class SingleChoiceAnswer {
    private String answer;
    private Boolean isSelected;

    public SingleChoiceAnswer(String answer, Boolean isSelected) {
        this.answer = answer;
        this.isSelected = isSelected;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
