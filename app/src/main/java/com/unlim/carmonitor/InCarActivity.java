package com.unlim.carmonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class InCarActivity extends AppCompatActivity {

    private TextView tvTitle, tvLicense, tvVIN, tvDescription;
    private EditText etOdometer;
    private Car currentCar;
    private ImageButton btnEditOdometer;
    private boolean isOdometerEditNow;
    private int currentOdometer;
    private Database database;
    private int carID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_car);
        carID = getIntent().getIntExtra(Const.INTENT_CURRENT_CAR_ID, 0);
        database = new Database(this);
        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        currentCar = database.selectCar(carID);
        currentOdometer = currentCar.getOdometer();
        fillFieldsWithData();
    }

    @Override
    public void onDestroy() {
        database = null;
        super.onDestroy();
    }

    public void editOdometer(View v) {
        int newOdometerValue = Integer.parseInt(etOdometer.getText().toString().replace(" ", ""));
        if(isOdometerEditNow) {
            if (newOdometerValue < currentOdometer) {
                showOnEditOdometerDialog(newOdometerValue);
            } else {
                currentOdometer = newOdometerValue;
                currentCar.setOdometer(currentOdometer);
                btnEditOdometer.setImageResource(R.drawable.outline_edit_black_18dp);
                etOdometer.setEnabled(false);
                isOdometerEditNow = false;
            }
        } else {
            btnEditOdometer.setImageResource(R.drawable.outline_done_outline_black_18dp);
            etOdometer.setEnabled(true);
            isOdometerEditNow = true;
        }
        saveCarToDB();
    }

    public void deleteCar(View v) {
        showOnDeleteDialog();
    }

    public void editCar(View v) {
        Intent intent = new Intent(this, NewCarActivity.class);
        intent.putExtra(Const.INTENT_IS_NEW_CAR, false);
        intent.putExtra(Const.INTENT_CURRENT_CAR, currentCar);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        currentCar = (Car) data.getSerializableExtra(Const.INTENT_CURRENT_CAR);
        saveCarToDB();
        fillFieldsWithData();
    }

    private void initUI() {
        tvTitle = findViewById(R.id.tv_in_car_title);
        tvLicense = findViewById(R.id.tv_in_car_license);
        tvVIN = findViewById(R.id.tv_in_car_vin);
        tvDescription = findViewById(R.id.tv_in_car_description);
        btnEditOdometer = findViewById(R.id.btn_edit_odometer);
        etOdometer = findViewById(R.id.et_in_car_odometer);
        etOdometer.setEnabled(false);
        etOdometer.addTextChangedListener(new IntTextWatcher(etOdometer));
    }

    private void fillFieldsWithData() {
        tvTitle.setText(currentCar.getBrand() + " " + currentCar.getModel());
        tvLicense.setText(currentCar.getLicense());
        tvVIN.setText(currentCar.getVin());
        tvDescription.setText(currentCar.getDescription());
        etOdometer.setText(String.valueOf(currentCar.getOdometer()));
    }

    private void showOnDeleteDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.attention));
        dialog.setMessage(getString(R.string.are_you_sure_delete_car));
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDeleteCarFromDB();
            }
        });
        dialog.show();
    }

    private void showOnEditOdometerDialog(final int newOdometerValue) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.attention));
        dialog.setMessage(getString(R.string.are_you_sure_edit_odometer));
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                etOdometer.setText(String.valueOf(currentOdometer));
                etOdometer.setSelection(etOdometer.getText().length());
                dialog.cancel();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSaveOdometerToDB(newOdometerValue);
            }
        });
        dialog.show();
    }

    private void onDeleteCarFromDB() {
        if (database != null) {
            database.deleteCar(currentCar);
        }
        finish();
    }

    private void onSaveOdometerToDB(int newOdometerValue) {
        currentOdometer = newOdometerValue;
        btnEditOdometer.setImageResource(R.drawable.outline_edit_black_18dp);
        etOdometer.setEnabled(false);
        isOdometerEditNow = false;
    }

    public void addRepair(View v) {
        Intent intent = new Intent(this, RepairActivity.class);
        intent.putExtra(Const.INTENT_CURRENT_CAR, currentCar);
        startActivity(intent);
    }

    public void addRefuel(View v) {

    }

    private void saveCarToDB() {
        database.updateCar(currentCar);
    }
}
