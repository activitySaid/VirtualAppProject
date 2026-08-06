package com.example.virtualapp.virtual;

import android.os.Build;
import com.example.virtualapp.device.DeviceProfile;

public class SystemHooks {
    
    private static void setStaticField(Class<?> clazz, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);
            field.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookBuildClass(DeviceProfile p) {
        try {
            if (p.getModel() != null) {
                setStaticField(Build.class, "MODEL", p.getModel());
            }
            if (p.getManufacturer() != null) {
                setStaticField(Build.class, "MANUFACTURER", p.getManufacturer());
            }
            if (p.getFingerprint() != null && p.getFingerprint().getSystemFingerprint() != null) {
                setStaticField(Build.class, "FINGERPRINT", p.getFingerprint().getSystemFingerprint());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hookSystemProperties(DeviceProfile p) {
        // برای اندروید ۱۲ به بالا
    }

    public static void hookTelephonyManager(DeviceProfile p) {
        // برای جعل IMEI
    }

    public static void setCustomFingerprint(String fp, DeviceProfile p) {
        if (p.getFingerprint() != null) {
            p.getFingerprint().setSystemFingerprint(fp);
            hookBuildClass(p);
        }
    }

    public static void setCustomImei(String imei1, String imei2, DeviceProfile profile) {
        if (profile.getNetwork() != null) {
            if (imei1 != null && imei1.length() == 15 && imei1.matches("\\d+")) {
                profile.getNetwork().imei1 = imei1;
            }
            if (imei2 != null && imei2.length() == 15 && imei2.matches("\\d+")) {
                profile.getNetwork().imei2 = imei2;
            }
            hookTelephonyManager(profile);
        }
    }
}