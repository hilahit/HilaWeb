package com.example.parkinson.features.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.fcm.MyFirebaseMessagingService;
import com.example.parkinson.features.main.adapters.MessagesListAdapter;
import com.example.parkinson.features.main.adapters.ReportsListAdapter;
import com.example.parkinson.features.notification.NotificationActivity;
import com.example.parkinson.features.on_boarding.OnBoardingActivity;
import com.example.parkinson.model.user_models.Patient;
import javax.inject.Inject;

public class MainFragment extends Fragment {

    @Inject
    MainViewModel mainViewModel;

    CardView medicineBtn;
    CardView questionnaireBtn;
    CardView medicCaseBtn;

    ImageView medicineBadge;
    ImageView questionnaireBadge;

    RecyclerView messagesList;
//    RecyclerView reportsList;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel.initData();
        initViews(view);
        initUi(view);
        initObservers();

    }

    private void initViews(View view) {
        medicineBtn = view.findViewById(R.id.mainFragMedicineBtn);
        questionnaireBtn = view.findViewById(R.id.mainFragQuestionnaireBtn);
        medicCaseBtn = view.findViewById(R.id.mainFragMedicCaseBtn);
        medicineBadge = view.findViewById(R.id.mainFragMedicineBadge);
        questionnaireBadge = view.findViewById(R.id.mainFragQuestionnaireBadge);

        messagesList= view.findViewById(R.id.recyclerMessages);
//        reportsList= view.findViewById(R.id.recyclerReports);
    }

    private void initUi(View view) {
        medicineBtn.setOnClickListener(v -> {
            openMedicineFragment(view);
        });
        questionnaireBtn.setOnClickListener(v -> {
            openQuestionnaireFragment(view);
        });
        medicCaseBtn.setOnClickListener(v -> {
            openMedicCaseFragment(view);
        });

    }

    private void initObservers() {
        mainViewModel.patientEvent.observe(getViewLifecycleOwner(), patient -> {
            handlePatientData(patient);
        });
//        mainViewModel.reportsData.observe(getViewLifecycleOwner(),data->{
//            ReportsListAdapter adapter = new ReportsListAdapter(data);
//            reportsList.setAdapter(adapter);
//        });
        mainViewModel.messagesData.observe(getViewLifecycleOwner(),data-> {
            MessagesListAdapter adapter = new MessagesListAdapter(data);
            messagesList.setAdapter(adapter);

        });

    }

    private void handlePatientData(Patient patient) {
        if (patient.getHasUnansweredQuestionnaire()) {
            questionnaireBadge.setVisibility(View.VISIBLE);
        } else {
            questionnaireBadge.setVisibility(View.INVISIBLE);
        }

        if (patient.getNeedToUpdateMedicine()) {
            medicineBadge.setVisibility(View.VISIBLE);
        } else {
            medicineBadge.setVisibility(View.INVISIBLE);
        }
    }

    /** Navigates to QuestionnaireFragment with NavigationController with isNewQuestionnaire Args **/
    private void openQuestionnaireFragment(View view){
        NavDirections action = MainFragmentDirections.actionMainFragmentToQuestionnaireFragment(mainViewModel.patientEvent.getValue().getHasUnansweredQuestionnaire());
        Navigation.findNavController(view).navigate(action);
    }

    private void openMedicineFragment(View view) {
        NavDirections action = MainFragmentDirections.actionMainFragmentToMyMedicinesFragment();
        Navigation.findNavController(view).navigate(action);
    }

    private void openMedicCaseFragment(View view) {
        NavDirections action = MainFragmentDirections.actionMainFragmentToMyMedicCaseFragment();
        Navigation.findNavController(view).navigate(action);
    }

//    private void openReportActivity() {
//        Toast.makeText(getActivity(), "bla bla", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(getActivity(), NotificationActivity.class);
//        startActivity(intent);
//    }
}
