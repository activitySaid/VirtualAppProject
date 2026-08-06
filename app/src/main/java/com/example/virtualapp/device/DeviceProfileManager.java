package com.example.virtualapp.device;

import android.content.Context;
import android.content.res.AssetManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceProfileManager {
    private static DeviceProfileManager instance;
    private final Map<String, DeviceProfile> profileMap = new HashMap<>();
    private final Gson gson = new Gson();
    private final Context context;

    private DeviceProfileManager(Context context) {
        this.context = context.getApplicationContext();
        loadAllProfilesFromSingleFile();
    }

    public static synchronized DeviceProfileManager getInstance(Context context) {
        if (instance == null) instance = new DeviceProfileManager(context);
        return instance;
    }

    private void loadAllProfilesFromSingleFile() {
        try {
            InputStream is = context.getAssets().open("profiles/all_devices.json");
            InputStreamReader reader = new InputStreamReader(is);
            Type type = new TypeToken<Map<String, List<DeviceProfile>>>(){}.getType();
            Map<String, List<DeviceProfile>> data = gson.fromJson(reader, type);
            
            List<DeviceProfile> devices = data.get("devices");
            if (devices != null) {
                for (DeviceProfile profile : devices) {
                    if (profile != null && profile.getModel() != null) {
                        profileMap.put(profile.getModel().toLowerCase(), profile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DeviceProfile getProfileByModel(String model) {
        return model == null ? null : profileMap.get(model.toLowerCase());
    }

    public List<String> getAllModels() {
        return new ArrayList<>(profileMap.keySet());
    }

    public String exportProfileToJson(String model) {
        DeviceProfile p = getProfileByModel(model);
        return p == null ? null : gson.toJson(p);
    }

    public boolean importProfileFromJson(String json) {
        try {
            Type t = new TypeToken<DeviceProfile>(){}.getType();
            DeviceProfile p = gson.fromJson(json, t);
            if (p != null && p.getModel() != null) {
                profileMap.put(p.getModel().toLowerCase(), p);
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public DeviceProfile generateRandomProfile(String model) {
        DeviceProfile base = getProfileByModel(model);
        if (base == null) return null;
        String json = gson.toJson(base);
        Type t = new TypeToken<DeviceProfile>(){}.getType();
        DeviceProfile newP = gson.fromJson(json, t);
        newP.getSoftware().serialNumber = "R" + System.currentTimeMillis() + "X";
        newP.getSoftware().androidId = Long.toHexString(System.currentTimeMillis() + (long)(Math.random()*1000000));
        if (newP.getNetwork() != null) {
            newP.getNetwork().imei1 = generateValidImei();
            newP.getNetwork().imei2 = generateValidImei();
        }
        return newP;
    }

    public String generateValidImei() {
        String tac = "356" + String.format("%05d", (int)(Math.random() * 100000));
        String snr = String.format("%06d", (int)(Math.random() * 1000000));
        return tac + snr + calculateLuhnChecksum(tac + snr);
    }

    private String calculateLuhnChecksum(String number) {
        int sum = 0;
        boolean alternate = false;
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
            alternate = !alternate;
        }
        int checksum = (10 - (sum % 10)) % 10;
        return String.valueOf(checksum);
    }
}