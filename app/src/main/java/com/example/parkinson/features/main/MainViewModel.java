package com.example.parkinson.features.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavDirections;

import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.di.MainScope;
import com.example.parkinson.model.enums.EQuestionType;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.MedicineCategory;
import com.example.parkinson.model.general_models.Report;
import com.example.parkinson.model.question_models.MultipleChoiceQuestion;
import com.example.parkinson.model.question_models.OpenQuestion;
import com.example.parkinson.model.question_models.Question;
import com.example.parkinson.model.question_models.Questionnaire;
import com.example.parkinson.model.user_models.Patient;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Repo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@MainScope
public class MainViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    MutableLiveData<Patient> patientEvent;
    MutableLiveData<Boolean> isLoading;

    List<Report> reports = new ArrayList<>();
    MutableLiveData<List<String>> messagesData = new MutableLiveData<>();
    MutableLiveData<List<Report>> reportsData = new MutableLiveData<>();

    /**
     * For navigation between fragments in main activity using NavigationComponent
     **/
    MutableLiveData<NavDirections> openFragmentEvent;

    /**
     * For navigation between activities
     **/
    MutableLiveData<OpenActivityEvent> openActivityEvent;

    public enum OpenActivityEvent {
        OPEN_ON_BOARDING_ACTIVITY
    }

    /**
     * Inject tells Dagger how to create instances of MainViewModel
     **/
    @Inject
    public MainViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
        patientEvent = new MutableLiveData<>();
        openFragmentEvent = new MutableLiveData<>();
        openActivityEvent = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
    }

    /**
     * Getting PatientDetails from firebase
     **/
    public void initData() {
        isLoading.setValue(true);
        handleUserDetails(userRepository.getPatientDetails());
        userRepository.getReportsList(setReportsListener());
    }

    private void handleUserDetails(Patient patientDetails) {
        patientEvent.postValue(patientDetails);
        List<String> messages = new ArrayList<>();
        if (patientDetails.getHasUnansweredQuestionnaire()) {
            messages.add("קיים שאלון חדש המחכה למענה");
        }
        if (patientDetails.getNeedToUpdateMedicine()) {
            messages.add("יש למלא רשימת תרופות");
        }
        if (!patientDetails.getNeedToUpdateMedicine() && !patientDetails.getHasUnansweredQuestionnaire()) {
            messages.add("אין הודעות חדשות");
        }
        messagesData.postValue(messages);
    }


    private ChildEventListener setReportsListener() {
        return new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Report report = snapshot.getValue(Report.class);
                    reports.add(report);
                    reportsData.postValue(reports);
                }
                isLoading.setValue(false);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                isLoading.setValue(false);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
    }

    /**
     * Logging out of current user and back to on boarding activity
     **/
    public void logOut() {
        userRepository.logout();
        openActivityEvent.postValue(OpenActivityEvent.OPEN_ON_BOARDING_ACTIVITY);
    }
}
