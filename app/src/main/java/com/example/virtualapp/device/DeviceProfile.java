package com.example.virtualapp.device;

import java.util.List;
import java.util.Map;

public class DeviceProfile {
    private String deviceName;
    private String manufacturer;
    private String model;
    private int androidVersion;
    private HardwareInfo hardware;
    private SoftwareInfo software;
    private NetworkInfo network;
    private SensorInfo sensors;
    private FingerprintInfo fingerprint;
    private GoogleServicesInfo googleServices;

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getAndroidVersion() { return androidVersion; }
    public void setAndroidVersion(int androidVersion) { this.androidVersion = androidVersion; }
    public HardwareInfo getHardware() { return hardware; }
    public void setHardware(HardwareInfo hardware) { this.hardware = hardware; }
    public SoftwareInfo getSoftware() { return software; }
    public void setSoftware(SoftwareInfo software) { this.software = software; }
    public NetworkInfo getNetwork() { return network; }
    public void setNetwork(NetworkInfo network) { this.network = network; }
    public SensorInfo getSensors() { return sensors; }
    public void setSensors(SensorInfo sensors) { this.sensors = sensors; }
    public FingerprintInfo getFingerprint() { return fingerprint; }
    public void setFingerprint(FingerprintInfo fingerprint) { this.fingerprint = fingerprint; }
    public GoogleServicesInfo getGoogleServices() { return googleServices; }
    public void setGoogleServices(GoogleServicesInfo googleServices) { this.googleServices = googleServices; }

    public static class HardwareInfo {
        public String processor, motherboard, platform, gpu, chipset, architecture;
        public int ramSizeGB, storageSizeGB;
    }

    public static class SoftwareInfo {
        public String buildId, buildVersion, securityPatch, bootloader, radio, serialNumber, androidId, googleServicesId;
    }

    public static class NetworkInfo {
        public String imei1, imei2, imsi, iccid, networkOperator, operatorName, simSerialNumber;
    }

    public static class SensorInfo {
        public List<String> availableSensors;
        public Map<String, Float> defaultValues;
    }

    public static class FingerprintInfo {
        public String systemFingerprint, hardwareFingerprint, vendorFingerprint;
        public String getSystemFingerprint() { return systemFingerprint; }
        public void setSystemFingerprint(String s) { this.systemFingerprint = s; }
    }

    public static class GoogleServicesInfo {
        public boolean gmsInstalled;
        public String gmsVersion, gsfId, adsId;
    }
}