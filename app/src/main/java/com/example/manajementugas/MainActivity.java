package com.example.manajementugas;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnToggleDaftar, btnToggleKalender;
    private TaskViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        btnToggleDaftar = findViewById(R.id.btn_toggle_daftar);
        btnToggleKalender = findViewById(R.id.btn_toggle_kalender);

        // Set default fragment
        if (savedInstanceState == null) {
            loadFragment(new ListFragment());
            updateToggleState(true);
        }

        btnToggleDaftar.setOnClickListener(v -> {
            updateToggleState(true);
            loadFragment(new ListFragment());
        });

        btnToggleKalender.setOnClickListener(v -> {
            updateToggleState(false);
            loadFragment(new CalendarFragment());
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }

    private void updateToggleState(boolean isDaftarActive) {
        int activeBg = ContextCompat.getColor(this, R.color.white);
        int activeText = ContextCompat.getColor(this, R.color.colorPrimary);
        int inactiveBg = ContextCompat.getColor(this, R.color.colorPrimary); // Use primary for inactive bg on primary header
        int inactiveText = ContextCompat.getColor(this, R.color.white);

        // Adjusting based on your layout design (white background for active, semi-transparent or darker for inactive)
        // Since activity_main has @color/colorPrimary as header background:
        
        if (isDaftarActive) {
            btnToggleDaftar.setBackgroundTintList(ColorStateList.valueOf(activeBg));
            btnToggleDaftar.setTextColor(activeText);
            btnToggleDaftar.setIconTint(ColorStateList.valueOf(activeText));

            btnToggleKalender.setBackgroundTintList(ColorStateList.valueOf(0x33FFFFFF)); // Semi-transparent white
            btnToggleKalender.setTextColor(inactiveText);
            btnToggleKalender.setIconTint(ColorStateList.valueOf(inactiveText));
        } else {
            btnToggleDaftar.setBackgroundTintList(ColorStateList.valueOf(0x33FFFFFF));
            btnToggleDaftar.setTextColor(inactiveText);
            btnToggleDaftar.setIconTint(ColorStateList.valueOf(inactiveText));

            btnToggleKalender.setBackgroundTintList(ColorStateList.valueOf(activeBg));
            btnToggleKalender.setTextColor(activeText);
            btnToggleKalender.setIconTint(ColorStateList.valueOf(activeText));
        }
    }
}
