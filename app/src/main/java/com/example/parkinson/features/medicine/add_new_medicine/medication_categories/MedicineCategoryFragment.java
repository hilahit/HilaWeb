package com.example.parkinson.features.medicine.add_new_medicine.medication_categories;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.medicine.MedicineViewModel;
import com.example.parkinson.features.medicine.add_new_medicine.medication_categories.MedicineCategoryAdapter.MedicineCategoryAdapterListener;
import com.example.parkinson.model.general_models.MedicineCategory;

import java.util.List;

import javax.inject.Inject;

public class MedicineCategoryFragment extends Fragment {

    @Inject
    MedicineViewModel medicineViewModel;

    RecyclerView recyclerView;
    MedicineCategoryAdapter adapter;

    public MedicineCategoryFragment(){
        super(R.layout.fragment_medicine_category_list);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initUi();
        initObservers();

    }

    private void initUi() {
        recyclerView = getView().findViewById(R.id.medicineCategoryFragRecycler);

        getView().findViewById(R.id.medicineCategoryFragExitBtn).setOnClickListener(v->{
            getActivity().onBackPressed();
        });
    }
    private void initObservers() {
//        medicineViewModel.categoryListData.observe(getViewLifecycleOwner(), categories -> {
//            handleListData(categories);
//        });
    }

    private void handleListData(List<MedicineCategory> categories){
        MedicineCategoryAdapterListener listener = getAdapterListener();
        adapter = new MedicineCategoryAdapter(categories ,listener);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private MedicineCategoryAdapterListener getAdapterListener() {
        return new MedicineCategoryAdapterListener() {
            @Override
            public void onCategoryClick(int chosenCategoryPosition) {
//                medicineViewModel.filterCategory(chosenCategoryPosition);
//                NavDirections action = MedicineCategoryFragmentDirections.actionMedicineCategoryFragmentToMedicineListFragment();
//                Navigation.findNavController(getView()).navigate(action);
            }
        };
    }


}
