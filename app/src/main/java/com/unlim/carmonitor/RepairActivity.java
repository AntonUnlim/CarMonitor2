package com.unlim.carmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class RepairActivity extends AppCompatActivity {

    private Car currentCar;
    private static  final int REQUEST_ACCESS_TYPE = 1;
    private Database database;
    private RecyclerView rvRepairList;
    private List<Repair> repairList;
    private RepairsListAdapter repairsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        database = new Database(this);
        currentCar = (Car)getIntent().getSerializableExtra(Const.INTENT_CURRENT_CAR);
        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        getRepairListFromDB();
    }

    public void addNewRepair(View v) {
        Intent intent = new Intent(this, NewRepairActivity.class);
        intent.putExtra(Const.INTENT_CURRENT_ODOMETER, currentCar.getOdometer());
        intent.putExtra(Const.INTENT_IS_NEW_REPAIR, true);
        startActivityForResult(intent, REQUEST_ACCESS_TYPE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ACCESS_TYPE) {
            if (resultCode == RESULT_OK) {
                Repair newRepair = (Repair)data.getSerializableExtra(Const.INTENT_REPAIR);
                boolean isNewRepair = data.getBooleanExtra(Const.INTENT_IS_NEW_REPAIR, false);
                if(isNewRepair) {
                    database.insertRepair(currentCar, newRepair);
                } else {
                    database.updateRepair(newRepair);
                }
                currentCar.setOdometer(newRepair.getOdometer());
                database.updateCar(currentCar);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initUI() {
        rvRepairList = findViewById(R.id.rv_repair_list);
        rvRepairList.setLayoutManager(new LinearLayoutManager(this));
        repairsListAdapter = new RepairsListAdapter();
        repairsListAdapter.setOnRecyclerItemClickListener(getOnItemClickListener());
        rvRepairList.setAdapter(repairsListAdapter);
    }

    private RepairsListAdapter.OnRecyclerItemClickListener getOnItemClickListener() {
        return new RepairsListAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(Repair repair) {
                Intent intent = new Intent(getApplicationContext(), NewRepairActivity.class);
                intent.putExtra(Const.INTENT_REPAIR, repair);
                intent.putExtra(Const.INTENT_IS_NEW_REPAIR, false);
                startActivity(intent);
            }
        };
    }

    private void getRepairListFromDB() {
        repairList = database.selectRepairsFromDB(currentCar);
        repairsListAdapter.setItems(repairList);
    }
}
