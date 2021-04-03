package com.example.bitkilerinisula;

import android.Manifest;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.bitkilerinisula.model.PlantModel;
import com.example.bitkilerinisula.repository.AppDatabase;
import com.example.bitkilerinisula.repository.PlantDao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddPlantFragment extends Fragment  {

    private EditText editTextName, editTextDate, editTextTime;
    private ImageButton imageButtonAddPhoto, imageButtonBack;
    private Button buttonAdd;
    private String selectedRoutine = "";
    private Bitmap bitmap;
    private String photoUrl;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private Uri imageUri;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private Calendar now = Calendar.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.add_fragment,container,false);



        editTextName = view.findViewById(R.id.editTextName);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextTime = view.findViewById(R.id.editTextTime);
        imageButtonAddPhoto = view.findViewById(R.id.imageButtonAddPhoto);
        imageButtonBack = view.findViewById(R.id.imageButtonBack);
        buttonAdd = view.findViewById(R.id.buttonAdd);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentHolder, new PlantListFragment(),"null").commit();
            }
        });

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA
            }, 100 );
        }

        imageButtonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setMessage("Fotoğraf Ekleyiniz");

                builder.setPositiveButton("kamera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //capture
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,100);
                    }

                });

                builder.setNegativeButton("galeri", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //galeri

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_DENIED){
                                //permission not granted, request it
                                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                                requestPermissions(permissions, PERMISSION_CODE);
                            }else {
                                //permission already granted
                                pickImageFromGallery();
                            }
                        }else {
                            pickImageFromGallery();
                        }

                    }
                });
                builder.create().show();

            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(java.util.Calendar.YEAR);
                int month = calendar.get(java.util.Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

               // DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                     //Notification için zaman çekme çabaları**

                        month = month+1;
                        String date = dayOfMonth+"/"+month+"/"+year;
                        editTextDate.setText(date);

                    }
                },year,month,day);
                datePickerDialog.setTitle("Tarih Seçiniz");
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Ayarla",datePickerDialog);
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"İptal",datePickerDialog);
                datePickerDialog.show();
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance(); // telefondan zaman değerlerini aldık
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            editTextTime.setText(hourOfDay+":"+minute);
                            now.set(Calendar.HOUR_OF_DAY, hourOfDay);
                            now.set(Calendar.MINUTE,minute);
                            now.set(Calendar.SECOND,0);


                    }
                },hour,minute,true);
                timePickerDialog.setTitle("Saat Seçiniz");
                timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Tamam",timePickerDialog);
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"İptal",timePickerDialog);

                timePickerDialog.show();
            }
        });


        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.plants_routine, R.layout.color_spinner_layout);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRoutine = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedRoutine = "";
            }
        });


        AppDatabase db = Room.databaseBuilder(getContext(),
                AppDatabase.class, "ozkan").allowMainThreadQueries().build();
        PlantDao plantDao = db.userDao();



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextName.getText().toString() == null || editTextTime.getText().toString() == null ||
                 editTextDate.getText().toString() == null || spinner.getSelectedItem().toString() == null || photoUrl == null){
                    Toast.makeText(getContext(), "Eksik bilgi",Toast.LENGTH_SHORT).show();
                    return;
                }



                PlantModel plantModel = new PlantModel(0,editTextName.getText().toString(),
                        editTextDate.getText().toString(),
                        editTextTime.getText().toString(),
                        selectedRoutine,photoUrl);


                plantDao.insertAll(plantModel);
                PlantListFragment plantListFragment = new PlantListFragment();

                //Alarm set etme denemeleri
                startAlarm(now, plantModel.getUid(),now.get(Calendar.HOUR_OF_DAY)*3600000+now.get(Calendar.MINUTE)*60000
                        ,spinner.getSelectedItemPosition());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentHolder, plantListFragment, "null")
                        .commit();

            }
        });

        return view;


    }




   private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }else {
                    Toast.makeText(getContext(),"Permission denied..!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 100 ){
            //Get capture image
            bitmap = (Bitmap) data.getExtras().get("data");
            //Set capture image to image view
            imageButtonAddPhoto.setImageBitmap(bitmap);

            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            Calendar cal = Calendar.getInstance();
            long time = cal.getTimeInMillis();
            photoUrl = time + ".jpg";

            File file = new File(directory, photoUrl);
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

        }else if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imageButtonAddPhoto.setImageURI(data.getData());

            ContextWrapper cw = new ContextWrapper(getContext());
            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            Calendar cal = Calendar.getInstance();
            long time = cal.getTimeInMillis();
            photoUrl = time + ".jpg";

            File file = new File(directory, photoUrl);
            if (!file.exists()) {
                Log.d("path", file.toString());
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startAlarm(Calendar c, int id, int startHour , int routine) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), id, intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startHour, routine*86400000,pendingIntent);
    }


}
