package com.example.parkinson.features.medic_case;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkinson.R;
import com.example.parkinson.features.main.MainActivity;
import com.example.parkinson.features.medicine.MyMedicinesFragmentDirections;
import com.example.parkinson.features.medicine.MyMedicinesMainAdapter;
import com.example.parkinson.features.medicine.MyMedicinesMainAdapter.MyMedicinesMainAdapterListener;
import com.example.parkinson.model.general_models.Medicine;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import javax.inject.Inject;

public class MyMedicCaseFragment extends Fragment {

    @Inject
    MedicCaseViewModel medicCaseViewModel;

    RecyclerView recyclerView;
    MyMedicCaseMainAdapter adapter;
    ImageButton addBtn;

    // Activity requests
    final int CAMERA_REQUEST = 1;
    final int SELECT_IMAGE = 2;
    final int WRITE_PERMISSION_REQUEST = 3;
    boolean permission = true;

    //camera and gallery
    Uri fileUri;
    Bitmap bitmap1, bitmap2;

    public MyMedicCaseFragment(){
        super(R.layout.fragment_medic_case);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        ((MainActivity) getActivity()).mainComponent.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        medicCaseViewModel.initMedicineData();
        initViews(view);
        initUi(view);
        initObservers();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getActivity(), "Im here", Toast.LENGTH_SHORT).show();
                buildSheetDialog();
            }
        });

    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.medicCaseFragRecycler);
        addBtn = view.findViewById(R.id.medicCaseFragAddBtn);

    }

    private void initUi(View view) {
        adapter = new MyMedicCaseMainAdapter(getMainAdapterListener());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        getView().findViewById(R.id.medicCaseFragExitBtn).setOnClickListener(v->{
            getActivity().onBackPressed();
        });

//        CardView addMedicine = view.findViewById(R.id.myMedicinesFragAddBtn);
//        addMedicine.setOnClickListener(v -> {
//            NavDirections action = MyMedicinesFragmentDirections.actionMedicineFragmentToMedicineCategoryFragment();
//            Navigation.findNavController(view).navigate(action);
//        });
    }

    private void initObservers() {
        medicCaseViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading-> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateLoadingScreen(isLoading);
        });
        medicCaseViewModel.myMedicationData.observe(getViewLifecycleOwner(), medicationCategories -> {
            //adapter.updateMedicineList(medicationCategories);
        });
    }

//
    private MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener getMainAdapterListener(){
        return new MyMedicCaseMainAdapter.MyMedicCaseMainAdapterListener() {
            @Override
            public void onMedicineClick(Medicine medicine) {
                NavDirections action = MyMedicinesFragmentDirections.actionMyMedicinesFragmentToSingleMedicineFrag(medicine);
                Navigation.findNavController(getView()).navigate(action);
            }
        };
    }

    private void buildSheetDialog() {

            final BottomSheetDialog bottomDialog = new BottomSheetDialog(getActivity(), R.style.BottomSheetDialogTheme);
            View bottomSheetView = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_sheet, null);

            LinearLayout takePic, selectPic;
            takePic = bottomSheetView.findViewById(R.id.select_take_pic);
            selectPic = bottomSheetView.findViewById(R.id.select_choose_pic);


            takePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    takePicture(CAMERA_REQUEST);
                    bottomDialog.dismiss();
                }
            });

            selectPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    askStoragePermissions(SELECT_IMAGE);
                    bottomDialog.dismiss();
                }
            });

            bottomDialog.setContentView(bottomSheetView);
            bottomDialog.show();


    }

    private void takePicture(int requestCode) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "from");
        fileUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, requestCode);
    }

    private void askStoragePermissions(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasReadPermission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED && hasReadPermission != PackageManager.PERMISSION_GRANTED) { //no permissions
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            } else { //have permissions
                openGallery(requestCode);
            }
        }
    }

    private void openGallery(int requestCode) {
        //TODO ask permissions here!!!
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_PERMISSION_REQUEST) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "אין הרשאה", Toast.LENGTH_SHORT).show();
                permission = false;
            } else {

                Toast.makeText(getActivity(), "ההרשאה ניתנה", Toast.LENGTH_SHORT).show();
                permission = true;

                openGallery(SELECT_IMAGE);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST) {

            if (resultCode == Activity.RESULT_OK) {

                bitmap2 = null;
                try {
                    bitmap1 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //isFromCamera = true;
                if (bitmap1 != null) {
                    //handleUpload(bitmap1, isProfilePicture);
                    Toast.makeText(getActivity(), "Camera", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                fileUri = null;
            }
        }

        if (requestCode == SELECT_IMAGE) {

            if (resultCode == Activity.RESULT_OK && permission == true) {

                fileUri = data.getData();
                bitmap1 = null;
                try {
                    bitmap2 = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getActivity().getContentResolver(), fileUri));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap2 != null) {
                    //handleUpload(bitmap2, isProfilePicture);
                    Toast.makeText(getActivity(), "GALLERY", Toast.LENGTH_SHORT).show();

                }

//                isFromCamera = false;
//                if (isFromCamera) {
//                    getActivity().getContentResolver().delete(fileUri, null, null);
//                }
            }
        }


    }

}
