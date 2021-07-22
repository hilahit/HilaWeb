package com.example.parkinson.features.questionnaire.single_question;

import com.example.parkinson.features.questionnaire.single_question.binders.QuestionBinderMultipleChoiceAnswer;
import com.example.parkinson.features.questionnaire.single_question.binders.QuestionBinderOpenAnswer;
import com.example.parkinson.features.questionnaire.single_question.binders.QuestionBinderOpenAnswer.QuestionBinderOpenAnswerListener;
import com.example.parkinson.features.questionnaire.single_question.binders.QuestionBinderSingleChoiceAnswer;
import com.example.parkinson.features.questionnaire.single_question.models.MultipleChoiceAnswer;
import com.example.parkinson.features.questionnaire.single_question.models.OpenAnswer;
import com.example.parkinson.features.questionnaire.single_question.models.SingleChoiceAnswer;

import java.util.List;

import mva2.adapter.ItemSection;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

/**
 * This is a very nice library for creating multiple view list - MultiViewAdapter
 * We have 2 view types :
 * 1. Open answer single ItemSection
 * 2. Multiple Choice answer ListSection - with single/multiple selection mode
 * for more information about implementation visit = https://devahamed.github.io/MultiViewAdapter/#/
 * enjoy :)
 */
public class SingleQuestionMainAdapter extends MultiViewAdapter {


    SingleQuestionMainAdapterListener adapterListener;
    /** Interface for both sections clicks **/
    interface SingleQuestionMainAdapterListener extends
            QuestionBinderOpenAnswerListener
            , QuestionBinderMultipleChoiceAnswer.QuestionBinderMultipleChoiceAnswerListener,
            QuestionBinderSingleChoiceAnswer.QuestionBinderSingleChoiceAnswerListener{}

    public SingleQuestionMainAdapter(SingleQuestionMainAdapterListener adapterListener) {
        this.adapterListener = adapterListener;
        initAdapter();
    }

    /** 2 type of sections - for open question and for multiple choice questions**/
    private final ListSection<MultipleChoiceAnswer> multipleChoiceAnswerSection = new ListSection<>();
    private final ListSection<SingleChoiceAnswer> singleChoiceAnswerSection = new ListSection<>();
    private final ItemSection<OpenAnswer> openAnswerSection = new ItemSection<>();

    private void initAdapter() {
        this.registerItemBinders(new QuestionBinderOpenAnswer(adapterListener),
                new QuestionBinderMultipleChoiceAnswer(adapterListener),
                new QuestionBinderSingleChoiceAnswer(adapterListener));

        this.addSection(multipleChoiceAnswerSection);
        this.addSection(singleChoiceAnswerSection);
        this.addSection(openAnswerSection);
        hideAllSections();
    }

    /**
     * updating data for singleChoiceAnswerSection from fragment
     **/
    public void updateSectionSingleChoiceAnswers(List<String> questionsList, List<String> answersList) {
        if (!questionsList.isEmpty()) {
            for (int i = 0; i < questionsList.size(); i++) {
                String answer = questionsList.get(i);
                Boolean isSelected = answersList.contains(answer);
                singleChoiceAnswerSection.add(new SingleChoiceAnswer(answer, isSelected));
            }
            singleChoiceAnswerSection.showSection();
        }
    }

    /**
     * updating data for multipleChoiceAnswerSection from fragment
     **/
    public void updateSectionMultiChoiceAnswers(List<String> questionsList, List<String> answersList) {
        if (!questionsList.isEmpty()) {
            for (int i = 0; i < questionsList.size(); i++) {
                String answer = questionsList.get(i);
                Boolean isSelected = answersList.contains(answer);
                multipleChoiceAnswerSection.add(new MultipleChoiceAnswer(answer, isSelected));
            }
            multipleChoiceAnswerSection.showSection();
        }
    }

    /**
     * updating data for openAnswerSection from fragment
     **/
    public void updateSectionOpenAnswer(String answer) {
        openAnswerSection.setItem(new OpenAnswer(answer));
        openAnswerSection.showSection();
    }

    /**
     * hiding all sections because only one selection is visible in a list
     **/
    private void hideAllSections() {
        singleChoiceAnswerSection.hideSection();
        multipleChoiceAnswerSection.hideSection();
        openAnswerSection.hideSection();
    }


}
