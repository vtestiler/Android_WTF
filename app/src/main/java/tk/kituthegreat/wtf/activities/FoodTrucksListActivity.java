package tk.kituthegreat.wtf.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.adapter.FoodTruckAdapter;
import tk.kituthegreat.wtf.constants.Constants;
import tk.kituthegreat.wtf.data.DataService;
import tk.kituthegreat.wtf.model.FoodTruck;
import tk.kituthegreat.wtf.view.ItemDecorator;

import static com.android.volley.Request.*;
import static com.android.volley.Request.Method.*;


public class FoodTrucksListActivity extends AppCompatActivity {

    // Variables
    private FoodTruckAdapter adapter;
    private FoodTruck foodTruck;
    private ArrayList<FoodTruck> trucks = new ArrayList<>();
    private static FoodTrucksListActivity foodTrucksListActivity;
    private FloatingActionButton addTruckBtn;
    public static final String EXTRA_ITEM_Truck = "TRUCK";
    SharedPreferences prefs;
    String authToken;


    public static FoodTrucksListActivity getFoodTrucksListActivity() {
        return foodTrucksListActivity;
    }

    public static void setFoodTrucksListActivity(FoodTrucksListActivity foodTrucksListActivity) {
        FoodTrucksListActivity.foodTrucksListActivity = foodTrucksListActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_trucks_list);

        foodTrucksListActivity.setFoodTrucksListActivity(this);
        addTruckBtn = (FloatingActionButton) findViewById(R.id.addTruckBtn);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        authToken = prefs.getString(Constants.AUTH_TOKEN, "Does not exist");

        addTruckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    loadAddTruck();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        TrucksDownloaded listener = new TrucksDownloaded() {
            @Override
            public void success(Boolean success) {
                if (success) {
                    setUpRecycler();
                }
            }
        };

        trucks = DataService.getInstance().downloadAllFoodTrucks(this, listener);
    }

    private void setUpRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_foodtruck);
        recyclerView.setHasFixedSize(true);
        adapter = new FoodTruckAdapter(trucks);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecorator(0, 0, 0, 10));


        final FoodTrucksListActivity.TruckDeleted listener = new FoodTrucksListActivity.TruckDeleted() {
            @Override
            public void success(Boolean success) {
                return;
            }
        };



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                foodTruck = adapter.getFoodTruckAt(viewHolder.getAdapterPosition());

                DataService.getInstance().deleteFoodTruck(foodTruck, getBaseContext(), authToken, listener);
                Toast.makeText(FoodTrucksListActivity.this, "Food Truck " + foodTruck.getName() + " deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    public interface TrucksDownloaded {
        void success(Boolean success);

    }

    public interface TruckDeleted {
        void success(Boolean success);

    }

    public void loadFoodTruckDetailActivity(FoodTruck truck) {
        Intent intent = new Intent(FoodTrucksListActivity.this, FoodTruckDetailActivity.class);
        intent.putExtra(FoodTrucksListActivity.EXTRA_ITEM_Truck, truck);
        startActivity(intent);
    }

    public  void loadAddTruck(){
        if (prefs.getBoolean(Constants.IS_LOGGED_IN, false)){
            Intent intent = new Intent(FoodTrucksListActivity.this, AddTruck.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(FoodTrucksListActivity.this, LoginActivity.class);
            Toast.makeText(getBaseContext(), "Please login to add food truck", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }



}
