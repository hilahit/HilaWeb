package com.example.parkinson.features.medicine.add_new_medicine.medication_categories;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.model.general_models.MedicineCategory;

import java.util.List;


public class MedicineCategoryAdapter extends RecyclerView.Adapter<MedicineCategoryAdapter.ViewHolder> {

    List<MedicineCategory> categories;
    MedicineCategoryAdapterListener listener;

    interface MedicineCategoryAdapterListener {
        void onCategoryClick(int chosenCategoryPosition);
    }

    public MedicineCategoryAdapter(List<MedicineCategory> categories, MedicineCategoryAdapterListener listener) {
        this.categories = categories;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine_category, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MedicineCategory category = categories.get(position);
        holder.categoryName.setText(category.getCategoryName());
        holder.itemView.setOnClickListener(v -> {
            listener.onCategoryClick(position);
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;

        public ViewHolder(View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.itemMedicineCategoryName);
        }
    }

}