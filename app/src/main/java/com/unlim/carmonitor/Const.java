package com.unlim.carmonitor;

import android.content.Intent;
import android.text.style.TtsSpan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class Const {
    public static final String INTENT_IS_NEW_CAR = "isNewCar";
    public static final String INTENT_IS_NEW_REPAIR = "isNewRepair";
    public static final String INTENT_CURRENT_CAR = "currentCar";
    public static final String INTENT_REPAIR_ID = "repairID";
    public static final String INTENT_REPAIR = "repair";
    public static final String INTENT_CURRENT_ODOMETER = "currentOdometer";
    public static final String INTENT_CURRENT_CAR_ID = "currentCarID";

    public static final int DIALOG_NEW_CAR_EXIT = 1;

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public class SQLQueries {
        public static final String selectCars = "SELECT " +
                Database.Columns.CAR_ID + ", " +
                Database.Columns.CAR_BRAND + ", " +
                Database.Columns.CAR_MODEL + ", " +
                Database.Columns.CAR_LICENSE + ", " +
                Database.Columns.CAR_ODOMETER + ", " +
                Database.Columns.CAR_VIN + ", " +
                Database.Columns.CAR_DESCRIPTION + " FROM " +
                Database.Tables.CAR_TABLE;
        public static final String selectOdometersByCar = "SELECT " +
                Database.Columns.ODOMETER_ID + ", " +
                Database.Columns.ODOMETER_DATETIME + ", " +
                Database.Columns.ODOMETER_VALUE + " FROM " +
                Database.Tables.ODOMETER_TABLE + " WHERE " +
                Database.Columns.ODOMETER_CAR_ID + " = ?";
        public static final String selectRepairsByCar = "SELECT " +
                Database.Columns.REPAIR_ID + ", " +
                Database.Columns.REPAIR_NAME  + ", " +
                Database.Columns.REPAIR_DESCRIPTION + ", " +
                Database.Columns.REPAIR_ODOMETER + ", " +
                Database.Columns.REPAIR_DATE + ", " +
                Database.Columns.REPAIR_CATALOG_NUMBER + ", " +
                Database.Columns.REPAIR_PART_PRICE + ", " +
                Database.Columns.REPAIR_WORK_PRICE + " FROM " +
                Database.Tables.REPAIR_TABLE + " WHERE " +
                Database.Columns.REPAIR_CAR_ID + " = ?";
        public static final String selectCar = "SELECT " +
                Database.Columns.CAR_ID + ", " +
                Database.Columns.CAR_BRAND + ", " +
                Database.Columns.CAR_MODEL + ", " +
                Database.Columns.CAR_LICENSE + ", " +
                Database.Columns.CAR_ODOMETER + ", " +
                Database.Columns.CAR_VIN + ", " +
                Database.Columns.CAR_DESCRIPTION + " FROM " +
                Database.Tables.CAR_TABLE + " WHERE " +
                Database.Columns.CAR_ID + " = ?";
        public static final String selectRepair = "SELECT " +
                Database.Columns.REPAIR_ID + ", " +
                Database.Columns.REPAIR_CAR_ID + ", " +
                Database.Columns.REPAIR_NAME  + ", " +
                Database.Columns.REPAIR_DESCRIPTION + ", " +
                Database.Columns.REPAIR_ODOMETER + ", " +
                Database.Columns.REPAIR_DATE + ", " +
                Database.Columns.REPAIR_CATALOG_NUMBER + ", " +
                Database.Columns.REPAIR_PART_PRICE + ", " +
                Database.Columns.REPAIR_WORK_PRICE + " FROM " +
                Database.Tables.REPAIR_TABLE + " WHERE " +
                Database.Columns.REPAIR_ID + " = ?";
    }

    public static Date getDateFromString(String dateStr) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(dateStr);
        } catch (ParseException e) {
            Calendar calendar = new GregorianCalendar(1900, Calendar.JANUARY, 1);
            return calendar.getTime();
        }
    }

    public static String getStringFromDate(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public static int getIntFromSpacedString(String str) {
        int result;
        try {
            result = Integer.parseInt(str.replace(" ", ""));
        } catch (Exception e) {
            result = 0;
        }
        return result;
    }

    public static float getFloatFromSpacedString(String str) {
        float result;
        try {
            result = Float.parseFloat(str.replace(" ", ""));
        } catch (Exception e) {
            result = 0.0f;
        }
        return result;
    }

    public static String getDecimalSpacedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = " " + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }
    }
}
