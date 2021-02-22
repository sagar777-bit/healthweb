package com.example.esp_health_monitor;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class registerActivity extends AppCompatActivity {
private FrameLayout fragmentlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fragmentlayout = findViewById(R.id.register_frame);
        setDefaultFragment(new signinFragment());

    }
    private void setDefaultFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fragmentlayout.getId(),fragment);
        fragmentTransaction.commit();
    }
}
