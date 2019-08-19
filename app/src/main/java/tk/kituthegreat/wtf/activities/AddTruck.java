package tk.kituthegreat.wtf.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.PhantomReference;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.constants.Constants;
import tk.kituthegreat.wtf.data.DataService;

public class AddTruck extends AppCompatActivity {

    // variables for add truck activity
    private EditText truckName;
    private EditText foodType;
    private EditText avgCost;
    private EditText lattitude;
    private EditText longitude;

    private Button addTruckBtn;
    private Button cancelBtn;

    String authToken;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_truck);

        truckName = (EditText) findViewById(R.id.new_truck_name);
        foodType = (EditText) findViewById(R.id.new_truck_food_type);
        avgCost = (EditText) findViewById(R.id.new_truck_avg_cost);
        lattitude = (EditText) findViewById(R.id.new_truck_latitude);
        longitude = (EditText) findViewById(R.id.new_truck_longitude);

        addTruckBtn = (Button) findViewById(R.id.add_truck_btn);
        cancelBtn = (Button) findViewById(R.id.cancel_truck_btn);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = prefs.getString(Constants.AUTH_TOKEN, "Does not exist");

        final AddTruckInterface listener = new AddTruckInterface() {
            @Override
            public void success(Boolean success) {
                    finish();
            }
        };

        addTruckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = truckName.getText().toString();
                final String type = foodType.getText().toString();
                final Double cost = Double.parseDouble(avgCost.getText().toString());
                final Double lat = Double.parseDouble(lattitude.getText().toString());
                final Double longi = Double.parseDouble(longitude.getText().toString());

                DataService.getInstance().addTruck(name, type, cost, lat, longi, getBaseContext(), listener, authToken);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public interface AddTruckInterface {
        void success (Boolean success);
    }
}
