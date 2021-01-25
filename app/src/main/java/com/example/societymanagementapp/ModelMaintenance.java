package com.example.societymanagementapp;

public class ModelMaintenance {
    String name,maintenanceType;

    public ModelMaintenance() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public ModelMaintenance(String name, String maintenanceType) {
        this.name = name;
        this.maintenanceType = maintenanceType;
    }
}
