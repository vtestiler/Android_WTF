package tk.kituthegreat.wtf.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.constants.Constants;
import tk.kituthegreat.wtf.data.DataService;
import tk.kituthegreat.wtf.model.FoodTruck;

public class ModifyTruck extends AppCompatActivity {

    // variables for modify truck activity
    private FoodTruck foodTruck;
    private EditText truckName;
    private EditText foodType;
    private EditText avgCost;
    private EditText lattitude;
    private EditText longitude;

    private Button modifyTruckBtn;
    private Button cancelBtn;

    String authToken;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_truck);

        foodTruck = getIntent().getParcelableExtra(FoodTruckDetailActivity.EXTRA_ITEM_Truck);

        truckName = (EditText) findViewById(R.id.new_truck_name);
        foodType = (EditText) findViewById(R.id.new_truck_food_type);
        avgCost = (EditText) findViewById(R.id.new_truck_avg_cost);
        lattitude = (EditText) findViewById(R.id.new_truck_latitude);
        longitude = (EditText) findViewById(R.id.new_truck_longitude);

        modifyTruckBtn = (Button) findViewById(R.id.modify_truck_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_truck_btn);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = prefs.getString(Constants.AUTH_TOKEN, "Does not exist");

        updateUI();

        final ModifyTruck.ModifyTruckInterface listener = new ModifyTruck.ModifyTruckInterface() {
            @Override
            public void success(Boolean success) {
                finish();
            }
        };

        modifyTruckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = truckName.getText().toString();
                final String type = foodType.getText().toString();
                final Double cost = Double.parseDouble(avgCost.getText().toString());
                final Double lat = Double.parseDouble(lattitude.getText().toString());
                final Double longi = Double.parseDouble(longitude.getText().toString());

                DataService.getInstance().modifyTruck(name, type, cost, lat, longi, foodTruck, getBaseContext(), listener, authToken);
                loadListActivity();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public interface ModifyTruckInterface {
        void success (Boolean success);
    }

    private void updateUI() {
        truckName.setText(foodTruck.getName());
        foodType.setText(foodTruck.getFoodType());
        avgCost.setText(Double.toString(foodTruck.getAvgCost()));
        lattitude.setText(Double.toString(foodTruck.getLatitude()));
        longitude.setText(Double.toString(foodTruck.getLongitude()));

    }

    public void loadListActivity() {
        Intent intent = new Intent(ModifyTruck.this, FoodTrucksListActivity.class);
        startActivity(intent);
    }
}
