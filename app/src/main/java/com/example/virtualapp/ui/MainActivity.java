package com.example.virtualapp.ui;

import android.os.Bundle;
import android.widget.*;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.virtualapp.virtual.AdvancedVirtualCore;
import com.example.virtualapp.device.DeviceProfileManager;
import com.example.virtualapp.device.DeviceProfile;
import com.example.virtualapp.R;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerModels;
    private EditText editCustomModel, editImei1, editImei2, editFingerprint;
    private Button btnRandomize, btnImportApp, btnExportProfile, btnImportProfile, btnApplyImei, btnApplyFingerprint, btnRandomizeImei;
    private Switch switchGooglePlay;
    private AdvancedVirtualCore virtualCore;
    private DeviceProfile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        virtualCore = AdvancedVirtualCore.getInstance(this);
        currentProfile = virtualCore.getCurrentProfile();

        spinnerModels = findViewById(R.id.spinner_models);
        editCustomModel = findViewById(R.id.edit_custom_model);
        editImei1 = findViewById(R.id.edit_imei1);
        editImei2 = findViewById(R.id.edit_imei2);
        editFingerprint = findViewById(R.id.edit_fingerprint);
        btnRandomize = findViewById(R.id.btn_randomize);
        btnImportApp = findViewById(R.id.btn_import_app);
        btnExportProfile = findViewById(R.id.btn_export_profile);
        btnImportProfile = findViewById(R.id.btn_import_profile);
        btnApplyImei = findViewById(R.id.btn_apply_imei);
        btnApplyFingerprint = findViewById(R.id.btn_apply_fingerprint);
        btnRandomizeImei = findViewById(R.id.btn_randomize_imei);
        switchGooglePlay = findViewById(R.id.switch_google_play);

        setupSpinner();
        setupListeners();
    }

    private void setupSpinner() {
        DeviceProfileManager manager = DeviceProfileManager.getInstance(this);
        List<String> models = manager.getAllModels();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, models);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModels.setAdapter(adapter);

        spinnerModels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedModel = (String) parent.getItemAtPosition(position);
                DeviceProfile profile = DeviceProfileManager.getInstance(MainActivity.this).getProfileByModel(selectedModel);
                if (profile != null) {
                    currentProfile = profile;
                    virtualCore.switchProfile(selectedModel);
                    updateUIWithProfile(profile);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupListeners() {
        btnRandomize.setOnClickListener(v -> {
            if (currentProfile != null) {
                DeviceProfileManager manager = DeviceProfileManager.getInstance(this);
                DeviceProfile randomProfile = manager.generateRandomProfile(currentProfile.getModel());
                if (randomProfile != null) {
                    currentProfile = randomProfile;
                    virtualCore.initializeWithProfile(randomProfile);
                    updateUIWithProfile(randomProfile);
                    Toast.makeText(this, "Profile randomized!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnApplyImei.setOnClickListener(v -> {
            String imei1 = editImei1.getText().toString().trim();
            String imei2 = editImei2.getText().toString().trim();
            if (!imei1.isEmpty() && currentProfile != null) {
                if (imei1.length() == 15 && imei1.matches("\\d+")) {
                    currentProfile.getNetwork().imei1 = imei1;
                } else {
                    Toast.makeText(this, "IMEI must be 15 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (!imei2.isEmpty() && currentProfile != null) {
                if (imei2.length() == 15 && imei2.matches("\\d+")) {
                    currentProfile.getNetwork().imei2 = imei2;
                } else {
                    Toast.makeText(this, "IMEI 2 must be 15 digits!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            virtualCore.initializeWithProfile(currentProfile);
            Toast.makeText(this, "IMEI Updated!", Toast.LENGTH_SHORT).show();
        });

        btnRandomizeImei.setOnClickListener(v -> {
            if (currentProfile != null) {
                DeviceProfileManager manager = DeviceProfileManager.getInstance(this);
                String newImei1 = manager.generateValidImei();
                String newImei2 = manager.generateValidImei();
                currentProfile.getNetwork().imei1 = newImei1;
                currentProfile.getNetwork().imei2 = newImei2;
                editImei1.setText(newImei1);
                editImei2.setText(newImei2);
                virtualCore.initializeWithProfile(currentProfile);
                Toast.makeText(this, "IMEI Randomized!", Toast.LENGTH_SHORT).show();
            }
        });

        btnApplyFingerprint.setOnClickListener(v -> {
            String fp = editFingerprint.getText().toString().trim();
            if (!fp.isEmpty() && currentProfile != null) {
                currentProfile.getFingerprint().setSystemFingerprint(fp);
                virtualCore.initializeWithProfile(currentProfile);
                Toast.makeText(this, "Fingerprint Updated!", Toast.LENGTH_SHORT).show();
            }
        });

        btnImportApp.setOnClickListener(v -> Toast.makeText(this, "Import APK - Coming Soon", Toast.LENGTH_SHORT).show());
        btnExportProfile.setOnClickListener(v -> Toast.makeText(this, "Export - Coming Soon", Toast.LENGTH_SHORT).show());
        btnImportProfile.setOnClickListener(v -> Toast.makeText(this, "Import - Coming Soon", Toast.LENGTH_SHORT).show());

        switchGooglePlay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) virtualCore.enableGooglePlay();
            else virtualCore.disableGooglePlay();
        });
    }

    private void updateUIWithProfile(DeviceProfile profile) {
        if (profile.getNetwork() != null) {
            editImei1.setText(profile.getNetwork().imei1 != null ? profile.getNetwork().imei1 : "");
            editImei2.setText(profile.getNetwork().imei2 != null ? profile.getNetwork().imei2 : "");
        }
        if (profile.getFingerprint() != null) {
            editFingerprint.setText(profile.getFingerprint().getSystemFingerprint() != null ? 
                profile.getFingerprint().getSystemFingerprint() : "");
        }
        if (profile.getModel() != null) {
            editCustomModel.setText(profile.getModel());
        }
    }
}