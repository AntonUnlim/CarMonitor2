package com.unlim.carmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvCarList;
    private List<Car> carList;
    private Database database;
    private CarsListAdapter carsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new Database(getApplicationContext());
        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        getCarListFromDB();
    }

    public void addNewCar(View view) {
        Intent intent = new Intent(this, NewCarActivity.class);
        intent.putExtra(Const.INTENT_IS_NEW_CAR, true);
        startActivity(intent);
    }

    private void initUI() {
        rvCarList = findViewById(R.id.rv_car_list);
        rvCarList.setLayoutManager(new LinearLayoutManager(this));
        carsListAdapter = new CarsListAdapter();
        carsListAdapter.setOnRecyclerItemClickListener(getOnItemClickListener());
        rvCarList.setAdapter(carsListAdapter);
    }

    private void getCarListFromDB() {
        carList = database.selectCarsFromDB();
        carsListAdapter.setItems(carList);
    }

    private CarsListAdapter.OnRecyclerItemClickListener getOnItemClickListener() {
        return new CarsListAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(Car car) {
                Intent intent = new Intent(getApplicationContext(), InCarActivity.class);
                intent.putExtra(Const.INTENT_CURRENT_CAR_ID, car.getID());
                startActivity(intent);
            }
        };
    }
}
