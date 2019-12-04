package com.unlim.carmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "car.db";
    private static final int DB_VERSION = 1;

    private final String sqlCreateCarTable = "CREATE TABLE " + Database.Tables.CAR_TABLE + " (" +
            Database.Columns.CAR_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Database.Columns.CAR_BRAND + " TEXT NOT NULL, " +
            Database.Columns.CAR_MODEL + " TEXT, " +
            Database.Columns.CAR_LICENSE + " TEXT, " +
            Database.Columns.CAR_ODOMETER + " INTEGER NOT NULL, " +
            Database.Columns.CAR_VIN + " TEXT, " +
            Database.Columns.CAR_DESCRIPTION + " TEXT);";
    private final String sqlDropCarTable = "DROP TABLE IF EXISTS " + Database.Tables.CAR_TABLE + ";";

    private final String sqlCreateOdometerTable = "CREATE TABLE " + Database.Tables.ODOMETER_TABLE + " (" +
            Database.Columns.ODOMETER_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Database.Columns.ODOMETER_CAR_ID + " INTEGER NOT NULL, " +
            Database.Columns.ODOMETER_DATETIME + " TEXT NOT NULL, " +
            Database.Columns.ODOMETER_VALUE + " INTEGER NOT NULL);";
    private final String sqlDropOdometerTable = "DROP TABLE IF EXISTS " + Database.Tables.ODOMETER_TABLE + ";";

    private final String sqlCreateRepairTable = "CREATE TABLE " + Database.Tables.REPAIR_TABLE + " (" +
            Database.Columns.REPAIR_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            Database.Columns.REPAIR_CAR_ID + " INTEGER NOT NULL, " +
            Database.Columns.REPAIR_NAME + " TEXT NOT NULL, " +
            Database.Columns.REPAIR_DESCRIPTION + " TEXT, " +
            Database.Columns.REPAIR_ODOMETER + " INTEGER NOT NULL, " +
            Database.Columns.REPAIR_DATE + " TEXT NOT NULL, " +
            Database.Columns.REPAIR_CATALOG_NUMBER + " TEXT, " +
            Database.Columns.REPAIR_PART_PRICE + " NUMERIC, " +
            Database.Columns.REPAIR_WORK_PRICE + " NUMERIC);";
    private final String sqlDropRepairTable = "DROP TABLE IF EXISTS " + Database.Tables.REPAIR_TABLE + ";";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreateCarTable);
        db.execSQL(sqlCreateOdometerTable);
        db.execSQL(sqlCreateRepairTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(sqlDropCarTable);
            db.execSQL(sqlDropOdometerTable);
            db.execSQL(sqlCreateCarTable);
            db.execSQL(sqlCreateOdometerTable);
        }
        if (oldVersion == 3 && newVersion == 4) {
            db.execSQL(sqlDropRepairTable);
            db.execSQL(sqlCreateRepairTable);
        }*/
        onCreate(db);
    }
}
