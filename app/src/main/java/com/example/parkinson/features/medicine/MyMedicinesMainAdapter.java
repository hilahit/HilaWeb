package com.example.parkinson.features.medicine;

import com.example.parkinson.features.medicine.binder.MedicineBinderEmptyList;
import com.example.parkinson.features.medicine.binder.MedicineBinderHeader;
import com.example.parkinson.features.medicine.binder.MedicineBinderMedicine;
import com.example.parkinson.features.medicine.models.CategoryEmpty;
import com.example.parkinson.model.general_models.Medicine;

import java.util.List;

import mva2.adapter.ItemSection;
import mva2.adapter.ListSection;
import mva2.adapter.MultiViewAdapter;

public class MyMedicinesMainAdapter extends MultiViewAdapter {

    MyMedicinesMainAdapterListener listener;

    public interface MyMedicinesMainAdapterListener extends MedicineBinderMedicine.MedicineBinderMedicineListener {
    }

    MyMedicinesMainAdapter(MyMedicinesMainAdapterListener listener) {
        this.listener = listener;
        init();
    }

    ItemSection<CategoryEmpty> emptyList = new ItemSection<>();

    private void init() {
        this.registerItemBinders(
                new MedicineBinderHeader(),
                new MedicineBinderMedicine(listener),
                new MedicineBinderEmptyList());
        emptyList.setItem(new CategoryEmpty());

        addSection(emptyList);
    }

    void updateMedicineList(List<Medicine> list) {
        this.removeAllSections();
        if(list.isEmpty()){
            addSection(emptyList);
        } else {
            ListSection<Medicine> medicationListSection = new ListSection<>();
            medicationListSection.set(list);
            this.addSection(medicationListSection);
        }

    }
}
