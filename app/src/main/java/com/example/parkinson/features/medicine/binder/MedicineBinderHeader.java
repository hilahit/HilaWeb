package com.example.parkinson.features.medicine.binder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.parkinson.R;
import mva2.adapter.ItemBinder;
import mva2.adapter.ItemViewHolder;

public class MedicineBinderHeader extends ItemBinder<String, MedicineBinderHeader.ViewHolder> {

    @Override
    public ViewHolder createViewHolder(ViewGroup parent) {
        return new ViewHolder(inflate(parent, R.layout.item_medice_header));
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof String;
    }

    @Override
    public void bindViewHolder(ViewHolder holder, String item) {
        holder.header.setText(item);
    }

    static class ViewHolder extends ItemViewHolder<String> {
        TextView header;

        public ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.itemHeaderName);
        }
    }
}
