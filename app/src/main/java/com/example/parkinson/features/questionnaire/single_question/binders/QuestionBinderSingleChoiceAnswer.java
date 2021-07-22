package com.example.parkinson.features.questionnaire.single_question.binders;


import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.example.parkinson.R;
import com.example.parkinson.features.questionnaire.single_question.models.MultipleChoiceAnswer;
import com.example.parkinson.features.questionnaire.single_question.models.SingleChoiceAnswer;

import java.util.ArrayList;
import java.util.List;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;


public class QuestionBinderSingleChoiceAnswer extends
        ItemBinder<SingleChoiceAnswer, QuestionBinderSingleChoiceAnswer.viewHolder> {

    QuestionBinderSingleChoiceAnswerListener listener;
    SingleChoiceAnswer selectedAnswer = null;

    public interface QuestionBinderSingleChoiceAnswerListener{
        void onChoiceChange( List<String> selectedAnswer);
    }

    public QuestionBinderSingleChoiceAnswer(QuestionBinderSingleChoiceAnswerListener listener){
        this.listener = listener;
    }

    @Override
    public viewHolder createViewHolder(ViewGroup parent) {
        return new viewHolder(inflate(parent, R.layout.item_question_multi_choice));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof SingleChoiceAnswer;
    }

    @Override
    public int getSpanSize(int maxSpanCount) {
        return maxSpanCount;
    }

    @Override
    public void bindViewHolder(final viewHolder holder, final SingleChoiceAnswer answer) {
        holder.answer.setText(answer.getAnswer());
        if (answer.getSelected()) {
            selectedAnswer = answer;
        }
        updateHolder(holder,answer);
        holder.itemView.setOnClickListener(v -> {
                    if (answer.getSelected()) {
                        answer.setSelected(false);
                        selectedAnswer = null;

                        listener.onChoiceChange(null);
                    } else {
                        if(selectedAnswer != null){
                            selectedAnswer.setSelected(false);
                        }
                        answer.setSelected(true);
                        List<String> answerList = new ArrayList<>();
                        answerList.add(answer.getAnswer());
                        listener.onChoiceChange(answerList);
                    }
                }
        );
    }

    void updateHolder(viewHolder holder, SingleChoiceAnswer answer){
        if (answer.getSelected()) {
            holder.mainLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_green));
            holder.checkIcon.setImageDrawable(AppCompatResources.getDrawable(holder.itemView.getContext(), R.drawable.ic_check));
            holder.checkIcon.setVisibility(View.VISIBLE);
        } else {
            holder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
            holder.checkIcon.setVisibility(View.INVISIBLE);
        }
    }


    static class viewHolder extends ItemViewHolder<SingleChoiceAnswer> {
        View view;
        LinearLayout mainLayout;
        ImageView checkIcon;
        TextView answer;

        public viewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            mainLayout = itemView.findViewById(R.id.itemQueMultiChoiceLayout);
            checkIcon = itemView.findViewById(R.id.itemQueMultiChoiceCheckIcon);
            answer = itemView.findViewById(R.id.itemQueMultiChoiceAnswer);
        }
    }

}