package com.unlim.carmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class NewRepairActivity extends AppCompatActivity {

    private EditText etRepairName, etRepairDescription, etRepairOdometer, etRepairCatalogNumber,
            etRepairPartPrice, etRepairWorkPrice;
    private TextView tvRepairDate;
    private Repair newRepair, currentRepair;
    private int odometer;
    private boolean isNewRepair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_repair);
        isNewRepair = getIntent().getBooleanExtra(Const.INTENT_IS_NEW_REPAIR, false);
        if(isNewRepair) {
            odometer = getIntent().getIntExtra(Const.INTENT_CURRENT_ODOMETER, 0);
        } else {
            currentRepair = (Repair)getIntent().getSerializableExtra(Const.INTENT_REPAIR);
            odometer = currentRepair.getOdometer();
        }
        initUI();
    }

    public void selectDate(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
        DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar c = Calendar.getInstance();
                c.set(year, month-1, dayOfMonth);
                setDateField(c.getTime());
            }
        };
    }

    public void setDateField(Date date) {
        tvRepairDate.setText(Const.getStringFromDate(date));
    }

    public void btnCancel(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void btnSave(View v) {
        String name = etRepairName.getText().toString();
        String description = etRepairDescription.getText().toString();
        int odometer = Const.getIntFromSpacedString(etRepairOdometer.getText().toString());
        Date date = Const.getDateFromString(tvRepairDate.getText().toString());
        String catalogNumber = etRepairCatalogNumber.getText().toString();
        float partPrice = Const.getFloatFromSpacedString(etRepairPartPrice.getText().toString());
        float workPrice = Const.getFloatFromSpacedString(etRepairWorkPrice.getText().toString());
        newRepair = new Repair(name, odometer);
        newRepair.setDescription(description);
        newRepair.setDate(date);
        newRepair.setCatalogNumber(catalogNumber);
        newRepair.setPartPrice(partPrice);
        newRepair.setWorkPrice(workPrice);
        Intent data = new Intent();
        data.putExtra(Const.INTENT_REPAIR, newRepair);
        data.putExtra(Const.INTENT_IS_NEW_REPAIR, isNewRepair);
        setResult(RESULT_OK, data);
        finish();
    }

    private void initUI() {
        etRepairName = findViewById(R.id.et_repair_name);
        etRepairDescription = findViewById(R.id.et_repair_description);
        etRepairOdometer = findViewById(R.id.et_repair_odometer);
        etRepairOdometer.addTextChangedListener(new NumberTextWatcher(etRepairOdometer));
        etRepairOdometer.setText(String.valueOf(odometer));
        etRepairCatalogNumber = findViewById(R.id.et_repair_catalog_number);
        etRepairPartPrice = findViewById(R.id.et_repair_part_price);
        etRepairPartPrice.addTextChangedListener(new NumberTextWatcher(etRepairPartPrice));
        etRepairWorkPrice = findViewById(R.id.et_repair_work_price);
        etRepairWorkPrice.addTextChangedListener(new NumberTextWatcher(etRepairWorkPrice));
        tvRepairDate = findViewById(R.id.tv_repair_date_value);
        if(!isNewRepair) {
            etRepairName.setText(currentRepair.getName());
            etRepairDescription.setText(currentRepair.getDescription());
            etRepairCatalogNumber.setText(currentRepair.getCatalogNumber());
            etRepairPartPrice.setText(Const.getDecimalSpacedString(String.valueOf(currentRepair.getPartPrice())));
            etRepairWorkPrice.setText(Const.getDecimalSpacedString(String.valueOf(currentRepair.getWorkPrice())));
            //tvRepairDate.setText(Const.getStringFromDate(currentRepair.getDate()));
            setDateField(currentRepair.getDate());
        } else {
            //tvRepairDate.setText(Const.getStringFromDate(Calendar.getInstance().getTime()));
            setDateField(Calendar.getInstance().getTime());
        }
    }
}
