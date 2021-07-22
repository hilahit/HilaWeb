package com.example.parkinson.features.medicine;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.medicine.MyMedicinesMainAdapter.MyMedicinesMainAdapterListener;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.MedicineCategory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MyMedicinesFragment extends Fragment {

    @Inject
    MedicineViewModel medicineViewModel;

    RecyclerView recyclerView;
    MyMedicinesMainAdapter adapter;

    public MyMedicinesFragment(){
        super(R.layout.fragment_my_medicines);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicineViewModel.initMedicineData();
        initViews(view);
        initUi(view);
        initObservers();

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.myMedicinesFragRecycler);
    }

    private void initUi(View view) {
        adapter = new MyMedicinesMainAdapter(getMainAdapterListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getView().findViewById(R.id.myMedicinesFragExitBtn).setOnClickListener(v->{
            getActivity().onBackPressed();
        });

        CardView addMedicine = view.findViewById(R.id.myMedicinesFragAddBtn);
        addMedicine.setOnClickListener(v -> {
            NavDirections action = MyMedicinesFragmentDirections.actionMedicineFragmentToMedicineCategoryFragment();
            Navigation.findNavController(view).navigate(action);
        });
    }

    private void initObservers() {
        medicineViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading-> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        medicineViewModel.myMedicationData.observe(getViewLifecycleOwner(), medicationCategories -> {
            adapter.updateMedicineList(medicationCategories);
        });
    }


    private MyMedicinesMainAdapterListener getMainAdapterListener(){
        return new MyMedicinesMainAdapterListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                NavDirections action = MyMedicinesFragmentDirections.actionMyMedicinesFragmentToSingleMedicineFrag(medicine);
                Navigation.findNavController(getView()).navigate(action);
            }
        };
    }

}
