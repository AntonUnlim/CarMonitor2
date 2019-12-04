package com.unlim.carmonitor;

import java.io.Serializable;
import java.util.Date;

public class Repair implements Serializable {
    private int id;
    private int carID;
    private String name;
    private String description;
    private Date date;
    private int odometer;
    private String catalogNumber;
    private float partPrice;
    private float workPrice;

    public Repair(String name, int odometer) {
        this.name = name;
        this.odometer = odometer;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return Const.getStringFromDate(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOdometer() {
        return odometer;
    }

    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public float getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(float partPrice) {
        this.partPrice = partPrice;
    }

    public float getWorkPrice() {
        return workPrice;
    }

    public void setWorkPrice(float workPrice) {
        this.workPrice = workPrice;
    }
}
