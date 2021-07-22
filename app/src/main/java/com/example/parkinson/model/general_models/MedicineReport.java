package com.example.parkinson.model.general_models;

public class MedicineReport {

    String medicineId;
    long takenTime;

    public MedicineReport() {
    }

    public MedicineReport(String medicineId, long takenTime) {
        this.medicineId = medicineId;
        this.takenTime = takenTime;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public long getTakenTime() {
        return takenTime;
    }

    public void setTakenTime(long takenTime) {
        this.takenTime = takenTime;
    }
}
