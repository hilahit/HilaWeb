package com.example.parkinson.model.general_models;

import com.example.parkinson.model.enums.EStatus;

public class Report {
    private Long reportTime;
    private EStatus status;
    private Boolean withHallucinations = false;

    public Report(){

    }

    public Report(Long reportTime, EStatus status) {
        this.reportTime = reportTime;
        this.status = status;
    }

    public Report(Long reportTime, EStatus status, Boolean withHallucinations) {
        this.reportTime = reportTime;
        this.status = status;
        this.withHallucinations = withHallucinations;
    }

    public Long getReportTime() {
        return reportTime;
    }

    public void setReportTime(Long reportTime) {
        this.reportTime = reportTime;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Boolean getHallucinations() {
        return withHallucinations;
    }

    public void setHallucinations(Boolean hallucinations) {
        withHallucinations = hallucinations;
    }
}
