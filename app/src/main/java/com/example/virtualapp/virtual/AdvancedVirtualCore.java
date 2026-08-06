package com.example.virtualapp.virtual;

import android.content.Context;
import com.example.virtualapp.device.DeviceProfile;
import com.example.virtualapp.device.DeviceProfileManager;
import com.example.virtualapp.virtual.VirtualCore;
import com.example.virtualapp.virtual.InstallResult;

public class AdvancedVirtualCore {
    private static AdvancedVirtualCore instance;
    private final VirtualCore core = VirtualCore.get();
    private final Context context;
    private DeviceProfile currentProfile;
    private boolean googlePlay = false;

    private AdvancedVirtualCore(Context context) {
        this.context = context.getApplicationContext();
        this.currentProfile = DeviceProfileManager.getInstance(context).getProfileByModel("poco_x4_pro");
    }

    public static synchronized AdvancedVirtualCore getInstance(Context context) {
        if (instance == null) instance = new AdvancedVirtualCore(context);
        return instance;
    }

    public void initializeWithProfile(DeviceProfile p) {
        this.currentProfile = p;
        SystemHooks.hookBuildClass(p);
        SystemHooks.hookTelephonyManager(p);
    }

    public InstallResult installApp(String apkPath) {
        if (currentProfile == null) return InstallResult.makeFailure("No profile");
        SystemHooks.hookBuildClass(currentProfile);
        return core.installPackage(apkPath, 0);
    }

    public void enableGooglePlay() { googlePlay = true; }
    public void disableGooglePlay() { googlePlay = false; }
    
    public void switchProfile(String model) {
        DeviceProfile p = DeviceProfileManager.getInstance(context).getProfileByModel(model);
        if (p != null) { 
            this.currentProfile = p; 
            SystemHooks.hookBuildClass(p);
            SystemHooks.hookTelephonyManager(p);
        }
    }
    
    public DeviceProfile getCurrentProfile() { return currentProfile; }
    public boolean isGooglePlayEnabled() { return googlePlay; }
}