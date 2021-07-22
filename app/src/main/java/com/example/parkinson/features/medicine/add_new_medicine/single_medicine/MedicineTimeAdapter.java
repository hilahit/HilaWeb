//package com.example.parkinson.features.medicine.add_new_medicine.single_medicine;
//
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.parkinson.R;
//import com.example.parkinson.model.general_models.Time;
//import java.util.Calendar;
//import java.util.List;
//
//
//public class MedicineTimeAdapter extends RecyclerView.Adapter<MedicineTimeAdapter.ViewHolder> {
//
//    List<Time> medicineTime;
//    CustomTimePickerDialog picker;
//
//    public MedicineTimeAdapter() {
////        this.medicineTime = medicineTime;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        // Create a new view, which defines the UI of the list item
//        View view = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.item_medicine_time, viewGroup, false);
//
//        return new ViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
//        Time time = medicineTime.get(position);
//        viewHolder.medicineTime.setText(time.fullTime());
//
//        viewHolder.itemView.setOnClickListener(v->{
//            final Calendar cldr = Calendar.getInstance();
//            int hour = cldr.get(Calendar.HOUR_OF_DAY);
//            int minutes = cldr.get(Calendar.MINUTE);
//            // time picker dialog
//            picker = new CustomTimePickerDialog(viewHolder.itemView.getContext(),
//                    new CustomTimePickerDialog.OnTimeSetListener() {
//                        @Override
//                        public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
//                            time.setHour(sHour);
//                            time.setMinutes(sMinute);
//                            viewHolder.medicineTime.setText(time.fullTime());
//                        }
//                    }, hour, minutes, true);
//            picker.show();
//        });
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return medicineTime.size();
//    }
//
//    public void addTime() {
//        Time time = new Time(0, 0);
//        medicineTime.add(time);
//        notifyItemInserted(medicineTime.size() - 1);
//    }
//
//    public void removeTime() {
//        medicineTime.remove(medicineTime.size() - 1);
//        notifyItemRemoved(medicineTime.size());
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView medicineTime;
//
//        public ViewHolder(View view) {
//            super(view);
//            medicineTime = view.findViewById(R.id.itemMedicineTime);
//
//        }
//    }
//}
