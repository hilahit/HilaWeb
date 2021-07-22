package com.example.parkinson.features.medicine.add_new_medicine.single_medicine;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.parkinson.data.DataRepository;
import com.example.parkinson.data.UserRepository;
import com.example.parkinson.model.general_models.Medicine;
import com.example.parkinson.model.general_models.Time;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SingleMedicineViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final DataRepository dataRepository;

    MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    MutableLiveData<Medicine> medicineData = new MutableLiveData<>();

    // @Inject tells Dagger how to create instances of MainViewModel
    @Inject
    public SingleMedicineViewModel(UserRepository userRepository, DataRepository dataRepository) {
        this.userRepository = userRepository;
        this.dataRepository = dataRepository;
    }

    public void initData(Medicine medicine) {
        List<Time> hoursArr = new ArrayList<>();
//        if(medicine.getHoursArr() != null){
//            for(Time time : medicine.getHoursArr()){
//                Time newTime = new Time(time.getMinutes(),time.getHour());
//                hoursArr.add(newTime);
//            }
//        }

        Medicine newMedicine = new Medicine(
                medicine.getId(), medicine.getName());
        medicineData.postValue(newMedicine);
    }

//    public void setDosage(Double dosage) {
//        medicineData.getValue().setDosage(dosage);
//    }

    public void saveMedicine() {
        userRepository.postMedication(medicineData.getValue());
    }

    public void deleteMedicine() {
        userRepository.deleteMedication(medicineData.getValue());
    }


}
