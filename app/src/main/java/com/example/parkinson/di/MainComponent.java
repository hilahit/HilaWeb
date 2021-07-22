package com.example.parkinson.di;

import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.main.MainFragment;
import com.example.parkinson.features.medic_case.MyMedicCaseFragment;
import com.example.parkinson.features.medicine.MyMedicinesFragment;
import com.example.parkinson.features.medicine.add_new_medicine.medication_categories.MedicineCategoryFragment;
import com.example.parkinson.features.medicine.add_new_medicine.medication_list.MedicineListFragment;
import com.example.parkinson.features.medicine.add_new_medicine.single_medicine.SingleMedicineFragment;
import com.example.parkinson.features.notification.NotificationActivity;
import com.example.parkinson.features.questionnaire.QuestionnaireFragment;
import com.example.parkinson.features.questionnaire.single_question.SingleQuestionFragment;

import dagger.Subcomponent;

@MainScope
@Subcomponent
public interface MainComponent {

    @Subcomponent.Factory
    interface Factory {
        MainComponent create();
    }

    void inject(MainActivity mainActivity);
    void inject(MainFragment mainFragment);
    void inject(QuestionnaireFragment questionnaireFragment);
    void inject(SingleQuestionFragment singleQuestionFragment);
    void inject(MyMedicinesFragment myMedicinesFragment);
    void inject(MedicineCategoryFragment medicineCategoryFragment);
    void inject(MedicineListFragment medicationListFragment);
    void inject(SingleMedicineFragment singleMedicineFragment);
    void inject(MyMedicCaseFragment myMedicCaseFragment);
}
