package com.unlim.carmonitor;

import java.io.Serializable;
import java.util.Objects;

public class Car implements Serializable {
    private int id;
    private String brand;
    private String model;
    private String license;
    private int odometer;
    private String vin;
    private String description;

    public Car(String brand, int odometer) {
        this.brand = brand;
        this.odometer = odometer;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getID() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return (model == null) ? "" : model;
    }

    public String getLicense() {
        return license;
    }

    public int getOdometer() {
        return odometer;
    }

    public String getVin() {
        return vin;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return brand.equals(car.brand) &&
                model.equals(car.model) &&
                license.equals(car.license);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand, model, license);
    }

    @Override
    public String toString() {
        return brand + " " + model + " #'" + license + "' - cur odom:" + odometer;
    }
}
