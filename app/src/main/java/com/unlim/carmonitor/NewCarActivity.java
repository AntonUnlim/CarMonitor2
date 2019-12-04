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

public class NewCarActivity extends AppCompatActivity {

    private EditText etBrand, etModel, etLicense, etOdometer, etVIN, etDescription;
    private TextView tvTitle;
    private ImageButton btnSave, btnCancel;
    private boolean isNewCar;
    private Car currentCar;
    private MyToast myToast;
    private int errColor;
    private String currentBrand, currentModel, currentLicense, currentVin, currentDescription;
    private int currentOdometer;
    private boolean isOdometerOk;

    protected void setIsOdometerOk(boolean ok) {
        this.isOdometerOk = ok;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_car);
        initUI();
        isNewCar = getIntent().getBooleanExtra(Const.INTENT_IS_NEW_CAR, true);
        if (!isNewCar) {
            tvTitle.setText(R.string.edit_car_title);
            currentCar = (Car)getIntent().getSerializableExtra(Const.INTENT_CURRENT_CAR);
            etBrand.setText(currentCar.getBrand());
            etModel.setText(currentCar.getModel());
            etLicense.setText(currentCar.getLicense());
            etOdometer.setText(String.valueOf(currentCar.getOdometer()));
            etVIN.setText(currentCar.getVin());
            etDescription.setText(currentCar.getDescription());

            currentBrand = currentCar.getBrand();
            currentModel = currentCar.getModel();
            currentLicense = currentCar.getLicense();
            currentVin = currentCar.getVin();
            currentDescription = currentCar.getDescription();
            currentOdometer = currentCar.getOdometer();
        } else {
            tvTitle.setText(R.string.add_new_car_title);
        }
    }

    public void onCarSave(View v) {
        String brand = etBrand.getText().toString();
        if (brand.isEmpty() || brand.trim().equals("")) {
            myToast.show(getString(R.string.toast_empty_car_brand));
            markErrField(etBrand);
            return;
        }

        String odometerStr = etOdometer.getText().toString();
        odometerValidation(odometerStr);
        if(!isOdometerOk) {
            return;
        }

        Car newCar;
        if (isNewCar) {
            newCar = new Car(brand, Const.getIntFromSpacedString(odometerStr));
        } else {
            newCar = currentCar;
            newCar.setBrand(brand);
            newCar.setOdometer(Const.getIntFromSpacedString(odometerStr));
        }

        String vin = etVIN.getText().toString();
        String regexp = "^[A-HJ-NPR-Z\\d]{13}\\d{4}$";
        if (!vin.isEmpty()) {
            if (vin.length() < 17) {
                myToast.show(getString(R.string.toast_vin_less_17));
                markErrField(etVIN);
                return;
            } else if (!vin.matches(regexp)) {
                myToast.show("VIN isn't valid!");
                markErrField(etVIN);
                return;
            } else {
                newCar.setVin(vin);
            }
        }

        String model = etModel.getText().toString();
        if (!model.isEmpty() && !model.trim().equals("")) {
            newCar.setModel(model);
        }

        String license = etLicense.getText().toString();
        if (!license.isEmpty() && !license.trim().equals("")) {
            newCar.setLicense(license);
        }

        String description = etDescription.getText().toString();
        if (!description.isEmpty() && !description.trim().equals("")) {
            newCar.setDescription(description);
        }

        Database database = new Database(getApplicationContext());
        if (isNewCar) {
            int newCarID = database.insertCar(newCar);
            newCar.setID(newCarID);
        } else {
            database.updateCar(newCar);
            Intent intent = new Intent();
            intent.putExtra(Const.INTENT_CURRENT_CAR, newCar);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public void onCarCancel(View v) {
        if (isNewCar) {
            if (!etBrand.getText().toString().isEmpty() ||
                    !etModel.getText().toString().isEmpty() ||
                    !etLicense.getText().toString().isEmpty() ||
                    !etVIN.getText().toString().isEmpty() ||
                    !etDescription.getText().toString().isEmpty() ||
                    !etOdometer.getText().toString().isEmpty()) {
                showOnExitDialog();
            } else {
                finish();
            }
        } else {
            if (!etBrand.getText().toString().equals(currentBrand) ||
                    !etModel.getText().toString().equals(currentModel) ||
                    !etLicense.getText().toString().equals(currentLicense) ||
                    !etVIN.getText().toString().equals(currentVin) ||
                    !etDescription.getText().toString().equals(currentDescription) ||
                    !etOdometer.getText().toString().replace(" ", "").equals(String.valueOf(currentOdometer))){
                showOnExitDialog();
            } else {
                finish();
            }
        }
    }

    private void initUI() {
        tvTitle = findViewById(R.id.tv_add_edit_new_car_title);
        myToast = new MyToast(getApplicationContext());
        etBrand = findViewById(R.id.et_car_brand);
        etModel = findViewById(R.id.et_car_model);
        etLicense = findViewById(R.id.et_car_license);

        etOdometer = findViewById(R.id.et_odometer);
        etOdometer.addTextChangedListener(new NumberTextWatcher(etOdometer));

        etVIN = findViewById(R.id.et_vin);
        etDescription = findViewById(R.id.et_car_description);
        btnSave = findViewById(R.id.btn_car_save);
        btnCancel = findViewById(R.id.btn_car_cancel);
        errColor = getResources().getColor(R.color.errFieldBgColor);
    }

    private void markErrField(EditText errField) {
        errField.setBackgroundColor(errColor);
        errField.requestFocus();
    }

    private void showOnExitDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.attention));
        dialog.setMessage(getString(R.string.are_you_sure_cancel_car));
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void odometerValidation(String newOdometer) {
        if (newOdometer.isEmpty()) {
            myToast.show(getString(R.string.toast_empty_odometer));
            markErrField(etOdometer);
            setIsOdometerOk(false);
            return;
        }
        int newOdometerInt = Const.getIntFromSpacedString(newOdometer);
        if (currentOdometer > newOdometerInt) {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(getString(R.string.attention));
            dialog.setMessage(getString(R.string.are_you_sure_edit_odometer));
            dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setIsOdometerOk(false);
                    dialog.cancel();
                }
            });
            dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setIsOdometerOk(true);
                    dialog.cancel();
                }
            });
            dialog.show();
        } else {
            setIsOdometerOk(true);
        }
    }
}
