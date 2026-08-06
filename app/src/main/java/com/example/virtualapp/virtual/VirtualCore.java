package com.example.virtualapp.virtual;

public class VirtualCore {
    private static final VirtualCore INSTANCE = new VirtualCore();

    public static VirtualCore get() {
        return INSTANCE;
    }

    public InstallResult installPackage(String path, int userId) {
        // TODO: real virtualization logic goes here later
        return InstallResult.makeSuccess("Installed (mock): " + path);
    }
}
