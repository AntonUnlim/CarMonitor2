package com.unlim.carmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Database {
    private static DatabaseHelper databaseHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;

    public Database (Context context) {
        databaseHelper = new DatabaseHelper(context);
        db = databaseHelper.getWritableDatabase();
    }

    class Tables {
        private Tables() {}

        static final String CAR_TABLE = "car";
        static final String ODOMETER_TABLE = "odometer";
        static final String REPAIR_TABLE = "repair";
    }

    public class Columns {
        private Columns(){}

        static final String CAR_ID = "_id";
        static final String CAR_BRAND = "brand";
        static final String CAR_MODEL = "model";
        static final String CAR_LICENSE = "license";
        static final String CAR_ODOMETER = "odometer";
        static final String CAR_VIN = "vin";
        static final String CAR_DESCRIPTION = "description";

        static final String ODOMETER_ID = "_id";
        static final String ODOMETER_CAR_ID = "car_id";
        static final String ODOMETER_DATETIME = "dateStamp";
        static final String ODOMETER_VALUE = "value";

        static final String REPAIR_ID = "_id";
        static final String REPAIR_CAR_ID = "car_id";
        static final String REPAIR_NAME = "name";
        static final String REPAIR_DESCRIPTION = "description";
        static final String REPAIR_ODOMETER = "odometer";
        static final String REPAIR_DATE = "date";
        static final String REPAIR_CATALOG_NUMBER = "catalogNumber";
        static final String REPAIR_PART_PRICE = "partPrice";
        static final String REPAIR_WORK_PRICE = "workPrice";
    }

    // Car
    public Car selectCar(int id) {
        Car car = null;
        cursor = db.rawQuery(Const.SQLQueries.selectCar, new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()) {
            int carId = cursor.getInt(cursor.getColumnIndex(Columns.CAR_ID));
            String brand = cursor.getString(cursor.getColumnIndex(Columns.CAR_BRAND));
            String model = cursor.getString(cursor.getColumnIndex(Columns.CAR_MODEL));
            String license = cursor.getString(cursor.getColumnIndex(Columns.CAR_LICENSE));
            int odometer = cursor.getInt(cursor.getColumnIndex(Columns.CAR_ODOMETER));
            String vin = cursor.getString(cursor.getColumnIndex(Columns.CAR_VIN));
            String description = cursor.getString(cursor.getColumnIndex(Columns.CAR_DESCRIPTION));
            car = new Car(brand, odometer);
            car.setID(carId);
            car.setModel(model);
            car.setLicense(license);
            car.setVin(vin);
            car.setDescription(description);
        }
        cursor.close();
        return car;
    }

    public List<Car> selectCarsFromDB() {
        List<Car> carList = new ArrayList<>();
        cursor = db.rawQuery(Const.SQLQueries.selectCars, null);
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Columns.CAR_ID));
            String brand = cursor.getString(cursor.getColumnIndex(Columns.CAR_BRAND));
            String model = cursor.getString(cursor.getColumnIndex(Columns.CAR_MODEL));
            String license = cursor.getString(cursor.getColumnIndex(Columns.CAR_LICENSE));
            int odometer = cursor.getInt(cursor.getColumnIndex(Columns.CAR_ODOMETER));
            String vin = cursor.getString(cursor.getColumnIndex(Columns.CAR_VIN));
            String description = cursor.getString(cursor.getColumnIndex(Columns.CAR_DESCRIPTION));
            Car car = new Car(brand, odometer);
            car.setID(id);
            car.setModel(model);
            car.setLicense(license);
            car.setVin(vin);
            car.setDescription(description);
            carList.add(car);
        }
        cursor.close();
        return carList;
    }

    public int insertCar(Car newCar) {
        ContentValues contentValues = getCarContentValues(newCar);
        long newCarID = db.insert(Tables.CAR_TABLE, null, contentValues);
        return (int) newCarID;
    }

    public void updateCar(Car car) {
        ContentValues contentValues = getCarContentValues(car);
        db.update(Tables.CAR_TABLE, contentValues, Columns.CAR_ID + " = ?", new String[] {String.valueOf(car.getID())});
    }

    public void deleteCar(Car car) {
        String carID = String.valueOf(car.getID());
        db.delete(Tables.REPAIR_TABLE, Columns.REPAIR_CAR_ID + " = ?", new String[] {carID});
        db.delete(Tables.CAR_TABLE, Columns.CAR_ID + " = ?", new String[] {carID});
    }

    private ContentValues getCarContentValues(Car car) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.CAR_BRAND, car.getBrand());
        contentValues.put(Columns.CAR_MODEL, car.getModel());
        contentValues.put(Columns.CAR_LICENSE, car.getLicense());
        contentValues.put(Columns.CAR_ODOMETER, car.getOdometer());
        contentValues.put(Columns.CAR_VIN, car.getVin());
        contentValues.put(Columns.CAR_DESCRIPTION, car.getDescription());
        return contentValues;
    }

    // Repair
    public Repair selectRepair(int id) {
        Repair repair = null;
        cursor = db.rawQuery(Const.SQLQueries.selectRepair, new String[] {String.valueOf(id)});
        if(cursor.moveToFirst()) {
            int repairID = cursor.getInt(cursor.getColumnIndex(Columns.REPAIR_ID));
            int carID = cursor.getInt(cursor.getColumnIndex(Columns.REPAIR_CAR_ID));
            String name = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_NAME));
            String description = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_DESCRIPTION));
            int odometer = cursor.getInt(cursor.getColumnIndex(Columns.REPAIR_ODOMETER));
            Date date = Const.getDateFromString(cursor.getString(cursor.getColumnIndex(Columns.REPAIR_DATE)));
            String catalogNumber = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_CATALOG_NUMBER));
            float partPrice = cursor.getFloat(cursor.getColumnIndex(Columns.REPAIR_PART_PRICE));
            float workPrice = cursor.getFloat(cursor.getColumnIndex(Columns.REPAIR_WORK_PRICE));
            repair = new Repair(name, odometer);
            repair.setID(repairID);
            repair.setCarID(carID);
            repair.setDescription(description);
            repair.setDate(date);
            repair.setCatalogNumber(catalogNumber);
            repair.setPartPrice(partPrice);
            repair.setWorkPrice(workPrice);
        }
        cursor.close();
        return repair;
    }

    public List<Repair> selectRepairsFromDB(Car car) {
        List<Repair> repairList = new ArrayList<>();
        cursor = db.rawQuery(Const.SQLQueries.selectRepairsByCar, new String[] {String.valueOf(car.getID())});
        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Columns.REPAIR_ID));
            String name = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_NAME));
            String description = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_DESCRIPTION));
            int odometer = cursor.getInt(cursor.getColumnIndex(Columns.REPAIR_ODOMETER));
            Date date = Const.getDateFromString(cursor.getString(cursor.getColumnIndex(Columns.REPAIR_DATE)));
            String catalogNumber = cursor.getString(cursor.getColumnIndex(Columns.REPAIR_CATALOG_NUMBER));
            float partPrice = cursor.getFloat(cursor.getColumnIndex(Columns.REPAIR_PART_PRICE));
            float workPrice = cursor.getFloat(cursor.getColumnIndex(Columns.REPAIR_WORK_PRICE));
            Repair repair = new Repair(name, odometer);
            repair.setID(id);
            repair.setCarID(car.getID());
            repair.setDescription(description);
            repair.setDate(date);
            repair.setCatalogNumber(catalogNumber);
            repair.setPartPrice(partPrice);
            repair.setWorkPrice(workPrice);
            repairList.add(repair);
        }
        cursor.close();
        return repairList;
    }

    public int insertRepair(Car car, Repair repair) {
        ContentValues contentValues = getRepairContentValues(repair);
        contentValues.put(Columns.REPAIR_CAR_ID, car.getID());
        long newRepairID = db.insert(Tables.REPAIR_TABLE, null, contentValues);
        return (int) newRepairID;
    }

    public void updateRepair(Repair repair) {
        ContentValues contentValues = getRepairContentValues(repair);
        db.update(Tables.REPAIR_TABLE, contentValues, Columns.REPAIR_ID + " = ?", new String[] {String.valueOf(repair.getID())});
    }

    public void deleteRepair(Repair repair) {
        db.delete(Tables.REPAIR_TABLE, Columns.REPAIR_ID + " = ?", new String[] {String.valueOf(repair.getID())});
    }

    private ContentValues getRepairContentValues(Repair repair) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.REPAIR_NAME, repair.getName());
        contentValues.put(Columns.REPAIR_DESCRIPTION, repair.getDescription());
        contentValues.put(Columns.REPAIR_ODOMETER, repair.getOdometer());
        contentValues.put(Columns.REPAIR_DATE, repair.getDateStr());
        contentValues.put(Columns.REPAIR_CATALOG_NUMBER, repair.getCatalogNumber());
        contentValues.put(Columns.REPAIR_PART_PRICE, repair.getPartPrice());
        contentValues.put(Columns.REPAIR_WORK_PRICE, repair.getWorkPrice());
        return contentValues;
    }
}
