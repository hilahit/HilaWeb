package com.example.parkinson.model.question_models;

import com.example.parkinson.model.enums.EChoiceType;
import com.example.parkinson.model.enums.EQuestionType;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
    private String title;
    private List<String> choices;
    private List <String> answers = new ArrayList<>();
    private EChoiceType choiceType;

    public MultipleChoiceQuestion() {
        super("", null);
    }

    public MultipleChoiceQuestion(String title, EQuestionType type, List<String> choices, List<String> ansPositions, EChoiceType choiceType) {
        super(title, type);
        this.choices = choices;
        this.answers = ansPositions;
        this.choiceType = choiceType;
    }

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public EChoiceType getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(EChoiceType choiceType) {
        this.choiceType = choiceType;
    }
}
