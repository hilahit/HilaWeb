package com.example.parkinson.features.main.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.model.general_models.Report;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ReportsListAdapter extends RecyclerView.Adapter<ReportsListAdapter.ViewHolder> {

    List<Report> reports;

    public ReportsListAdapter(List<Report> reports){
        this.reports = reports;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Report report = reports.get(position);
        switch (report.getStatus()){
            case On:
                holder.report.setText("דווח: On");
                break;
            case Off:
                holder.report.setText("דווח: OFF");
                break;
            case Dyskinesia:
                holder.report.setText("דווח: דסקנזיה");
                break;
            case Hallucination:
                holder.report.setText("דווח: הזיות");
                break;
        }
        String dateString = new SimpleDateFormat("hh:mm", Locale.US).format(new Date(report.getReportTime()));
        holder.time.setText( "שעת דיווח: " + dateString);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView report;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            report = itemView.findViewById(R.id.itemReport);
            time = itemView.findViewById(R.id.itemReportTime);
        }
    }

}