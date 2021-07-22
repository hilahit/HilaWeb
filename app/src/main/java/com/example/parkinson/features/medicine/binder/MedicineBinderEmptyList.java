package com.example.parkinson.features.medicine.binder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.parkinson.R;
import com.example.parkinson.features.medicine.models.CategoryEmpty;

import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class MedicineBinderEmptyList extends ItemBinder<CategoryEmpty, MedicineBinderEmptyList.ViewHolder> {

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(inflate(parent, R.layout.item_empty_medicine_list));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof CategoryEmpty;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, CategoryEmpty item) {

    }

    static class ViewHolder extends ItemViewHolder<CategoryEmpty> {

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
