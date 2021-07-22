package com.example.parkinson.model.general_models;

import java.util.List;

public class MedicineCategory {

   String categoryName;
   List<Medicine> medicineList;

    public MedicineCategory() {
    }

    public MedicineCategory(String categoryName, List<Medicine> medicineList) {
        this.categoryName = categoryName;
        this.medicineList = medicineList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<Medicine> getMedicineList() {
        return medicineList;
    }

    public void setMedicineList(List<Medicine> medicineList) {
        this.medicineList = medicineList;
    }
}
