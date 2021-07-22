package com.example.parkinson.features.medicine.add_new_medicine.medication_list;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.MedicineCategory;

import java.util.HashMap;


public class MedicineListAdapter extends RecyclerView.Adapter<MedicineListAdapter.ViewHolder> {

    MedicineCategory category;
    MedicineListAdapterListener listener;


    interface MedicineListAdapterListener {
        void onMedicineClick(Medicine medicine);
    }

    public MedicineListAdapter(MedicineCategory category, MedicineListAdapterListener listener) {
        this.category = category;
        this.listener = listener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return category.getMedicineList().size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Medicine medicine = category.getMedicineList().get(position);
        holder.name.setText(medicine.getName());
        holder.itemView.setOnClickListener(v -> {
            listener.onMedicineClick(medicine);
        });

//        holder.dosage.setText(medicine.dosageString());
//        if (medicine.getDosage() > 0) {
//            holder.mainLayout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.light_green));
//            if (medicine.getHoursArr().size() == 1) {
//                holder.dosage.setText(holder.dosage.getText() + " ," + "פעם אחת ביום");
//            } else {
//                holder.dosage.setText(holder.dosage.getText() + " ," + medicine.getHoursArr().size() + " פעמים ביום");
//            }
//        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        LinearLayout mainLayout;
        TextView dosage;

        public ViewHolder(View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.itemMedicineLayout);
            name = itemView.findViewById(R.id.itemMedicineName);
            dosage = itemView.findViewById(R.id.itemMedicineDosage);
        }
    }

}