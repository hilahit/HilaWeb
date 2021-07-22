package com.example.parkinson.features.questionnaire.single_question.models;

import com.example.parkinson.model.enums.EChoiceType;
import com.example.parkinson.model.enums.EQuestionType;

public class MultipleChoiceAnswer {
    private String answer;
    private Boolean isSelected;

    public MultipleChoiceAnswer(String answer, Boolean isSelected) {
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
