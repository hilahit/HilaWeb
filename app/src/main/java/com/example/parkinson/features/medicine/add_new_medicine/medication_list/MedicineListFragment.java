package com.example.parkinson.features.medicine.add_new_medicine.medication_list;

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
import com.example.parkinson.features.medicine.add_new_medicine.medication_list.MedicineListAdapter.MedicineListAdapterListener;
import com.example.parkinson.model.general_models.Medicine;

import javax.inject.Inject;

public class MedicineListFragment extends Fragment {

    @Inject
    MedicineViewModel medicineViewModel;

    RecyclerView recyclerView;
    MedicineListAdapter adapter;
//    MedicineCategory category;

    public MedicineListFragment(){
        super(R.layout.fragment_medicine_list);
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
        recyclerView = getView().findViewById(R.id.medicineListFragRecycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        getView().findViewById(R.id.medicineListFragExitBtn).setOnClickListener(v->{
            getActivity().onBackPressed();
        });
    }

    private MedicineListAdapterListener getAdapterListener() {
        return new MedicineListAdapterListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                NavDirections action = MedicineListFragmentDirections.actionMedicineListFragmentToSingleMedicineFrag(medicine);
                Navigation.findNavController(getView()).navigate(action);
            }
        };
    }

    private void initObservers() {
//        medicineViewModel.filteredCategory.observe(getViewLifecycleOwner(), category->{
//            MedicineListAdapterListener listener = getAdapterListener();
//            adapter = new MedicineListAdapter(category, listener);
//            recyclerView.setAdapter(adapter);
//
//            TextView title = getView().findViewById(R.id.medicineListFragTitle);
//            title.setText(category.getCategoryName());
//        });
    }
}
