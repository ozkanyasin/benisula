package com.example.bitkilerinisula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Fragment tempFragment;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setItemIconTintList(null);

        loadFragments(new PlantListFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                tempFragment = null;
                switch (item.getItemId()){
                    case R.id.action1:
                        tempFragment = new PlantListFragment();
                        break;
                    case R.id.action2:
                        tempFragment = new PlantTimeLineFragment();
                        break;
                    case R.id.action3:
                        tempFragment = new AddPlantFragment();
                        break;
                }
                return loadFragments(tempFragment);
            }
        });

        getResources().getString(R.string.once_a_day);

    }

    public boolean loadFragments(Fragment fragment){
        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHolder, fragment)
                    .addToBackStack(null).commit();
        }
        return true;
    }

}