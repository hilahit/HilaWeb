package com.example.parkinson.features.medicine.add_new_medicine.single_medicine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.model.general_models.Medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class SingleMedicineFragment extends Fragment {

    @Inject
    SingleMedicineViewModel singleMedicineViewModel;

    private EditText valueEt;
    private Medicine medicine;

    public SingleMedicineFragment() {
        super(R.layout.fragment_single_medicine);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            singleMedicineViewModel.initData(
                    SingleMedicineFragmentArgs.fromBundle(getArguments()).getMedicine()
            );
        }

        initUi();
        initObservers();

    }

    private void initUi() {
        getView().findViewById(R.id.singleMedicineFragExitBtn).setOnClickListener(v -> {
            getActivity().onBackPressed();
        });


//        getView().findViewById(R.id.myMedicinesFragDeleteButton).setOnClickListener(v->{
//            singleMedicineViewModel.deleteMedicine();
//            getActivity().onBackPressed();
//        });

        getView().findViewById(R.id.myMedicinesFragSaveButton).setOnClickListener(v -> {
            this.medicine.setValue(Double.valueOf(valueEt.getText().toString()));
            singleMedicineViewModel.saveMedicine();
            getActivity().onBackPressed();
        });
    }

    private void initObservers() {
        singleMedicineViewModel.medicineData.observe(getViewLifecycleOwner(), it -> {
            handleMedicineData(it);
        });
    }

    private void handleMedicineData(Medicine medicine) {
        TextView title = getView().findViewById(R.id.singleMedicineFragTitle);
        title.setText(medicine.getName());

        valueEt = getView().findViewById(R.id.singleMedicineFragSpinner);

        this.medicine = medicine; //to pull value later

//        RecyclerView recyclerView = getView().findViewById(R.id.singleMedicineFragRecycler);
//        TextView timeNumber = getView().findViewById(R.id.singleMedicineFragTimeNumber);
//        ImageButton addButton = getView().findViewById(R.id.singleMedicineFragAddTime);
//        ImageButton removeButton = getView().findViewById(R.id.singleMedicineFragRemoveTime);
//        MedicineTimeAdapter adapter = new MedicineTimeAdapter(medicine.getHoursArr());
//        recyclerView.setAdapter(adapter);
//
//        timeNumber.setText(String.valueOf(adapter.getItemCount()));
//
//        addButton.setOnClickListener(v->{
//            adapter.addTime();
//            timeNumber.setText(String.valueOf(adapter.getItemCount()));
//        });
//        removeButton.setOnClickListener(v->{
//            adapter.removeTime();
//            timeNumber.setText(String.valueOf(adapter.getItemCount()));
//        });
//        initDosageSpinner(medicine.getDosage());
    }

//    private void initDosageSpinner(Double selectedDosage) {
//        HashMap<Double, String> dosagesHash = new HashMap<>();
//        dosagesHash.put(0.25, "רבע כדור");
//        dosagesHash.put(0.50,"חצי כדור");
//        dosagesHash.put(0.75, "שלושת רבעי כדור");
//        dosagesHash.put(1.00, "כדור בודד");
//        dosagesHash.put(1.25, "כדור ורבע");
//        dosagesHash.put(1.50, "כדור וחצי");
//        dosagesHash.put(1.75, "כדור ושלושת רבעי");
//        dosagesHash.put(2.00, "שני כדורים");
//
//        final Spinner dosageSpinner = (Spinner) getView().findViewById(R.id.singleMedicineFragSpinner);
//        List<String> dosageArr= new ArrayList <>();
//        dosageArr.add(dosagesHash.get(0.25));
//        dosageArr.add(dosagesHash.get(0.50));
//        dosageArr.add(dosagesHash.get(0.75));
//        dosageArr.add(dosagesHash.get(1.00));
//        dosageArr.add(dosagesHash.get(1.25));
//        dosageArr.add(dosagesHash.get(1.50));
//        dosageArr.add(dosagesHash.get(1.75));
//        dosageArr.add(dosagesHash.get(2.00));
//        if(selectedDosage == null || selectedDosage == 0.0){
//            dosageArr.add("בחר מינון");
//        } else {
//            dosageArr.add(dosagesHash.get(selectedDosage));
//        }
//
//        final int dosageArrSize = dosageArr.size() - 1;
//
//        ArrayAdapter<String> dosageAdapter = new ArrayAdapter<String>(requireContext(), R.layout.spinner_item, dosageArr){
//            @Override
//            public int getCount() {
//                return(dosageArrSize); // Truncate the list
//            }
//        };
//
//
//
//        dosageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dosageSpinner.setAdapter(dosageAdapter);
//        dosageSpinner.setSelection(dosageArrSize);
//        dosageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        singleMedicineViewModel.setDosage(0.25);
//                        break;
//                    case 1:
//                        singleMedicineViewModel.setDosage(0.50);
//                        break;
//                    case 2:
//                        singleMedicineViewModel.setDosage(0.75);
//                        break;
//                    case 3:
//                        singleMedicineViewModel.setDosage(1.00);
//                        break;
//                    case 4:
//                        singleMedicineViewModel.setDosage(1.25);
//                        break;
//                    case 5:
//                        singleMedicineViewModel.setDosage(1.50);
//                        break;
//                    case 6:
//                        singleMedicineViewModel.setDosage(1.75);
//                        break;
//                    case 7:
//                        singleMedicineViewModel.setDosage(2.00);
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//    }
}
