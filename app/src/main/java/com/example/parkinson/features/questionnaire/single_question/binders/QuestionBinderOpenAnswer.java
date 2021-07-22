package com.example.parkinson.features.questionnaire.single_question.binders;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.parkinson.R;
import com.example.parkinson.features.questionnaire.single_question.models.OpenAnswer;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;


public class QuestionBinderOpenAnswer extends
        ItemBinder<OpenAnswer, QuestionBinderOpenAnswer.viewHolder> {

    public interface QuestionBinderOpenAnswerListener {
        void onOpenAnswerChanged(String answer);
    }

    QuestionBinderOpenAnswerListener listener;
    public QuestionBinderOpenAnswer(QuestionBinderOpenAnswerListener listener) {
        this.listener = listener;
    }

    @Override
    public viewHolder createViewHolder(ViewGroup parent) {
        return new viewHolder(inflate(parent, R.layout.item_question_open));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof OpenAnswer;
    }

    @Override public int getSpanSize(int maxSpanCount) {
        return maxSpanCount;
    }

    @Override
    public void bindViewHolder(final viewHolder holder, final OpenAnswer answer) {
        holder.answer.setText(answer.getAnswer());
        holder.answer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                String answer = s.toString();
                listener.onOpenAnswerChanged(answer);
            }
        });

    }

    static class viewHolder extends ItemViewHolder<OpenAnswer> {
        View view;
        EditText answer;


        public viewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            answer = itemView.findViewById(R.id.itemQueOpenAnswer);
        }
    }

}