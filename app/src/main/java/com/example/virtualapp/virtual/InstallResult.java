package com.example.virtualapp.virtual;

public class InstallResult {
    public final boolean success;
    public final String message;

    private InstallResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static InstallResult makeSuccess(String message) {
        return new InstallResult(true, message);
    }

    public static InstallResult makeFailure(String message) {
        return new InstallResult(false, message);
    }
}
