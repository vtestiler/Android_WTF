package tk.kituthegreat.wtf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import tk.kituthegreat.wtf.model.FoodTruck;
import tk.kituthegreat.wtf.view.ItemDecorator;

import static com.android.volley.Request.*;
import static com.android.volley.Request.Method.*;


public class FoodTrucksListActivity extends AppCompatActivity {

    // Variables
    private FoodTruckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_trucks_list);

        String url = "https://kituthegreat.tk/api/v1/foodtruck";
        //String url = "http://10.0.2.2:3005/v1/foodtruck";
        final ArrayList<FoodTruck> foodTruckList = new ArrayList<>();
        System.out.println("Testing");

        final JsonArrayRequest getTrucks = new JsonArrayRequest(GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());

                try {
                    JSONArray foodTrucks = response;
                    for (int x = 0; x < foodTrucks.length(); x++) {
                        JSONObject foodTruck = foodTrucks.getJSONObject(x);
                        String name = foodTruck.getString("name");
                        String id = foodTruck.getString("_id");
                        String foodType = foodTruck.getString("foodtype");
                        Double avgCost = foodTruck.getDouble("avgcost");

                        JSONObject geometry = foodTruck.getJSONObject("geometry");
                        JSONObject coordinates = geometry.getJSONObject("coordinates");
                        Double longitude = coordinates.getDouble("long");
                        Double lattitude = coordinates.getDouble("lat");

                        FoodTruck newFoodTruck = new FoodTruck(id, name, foodType, avgCost, lattitude, longitude);
                        foodTruckList.add(newFoodTruck);
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }

                System.out.println("This is a 1st food truck name: " + foodTruckList.get(0).getName());
                System.out.println("This is a 2nd food truck name: " + foodTruckList.get(1).getName());

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_foodtruck);
                recyclerView.setHasFixedSize(true);
                adapter = new FoodTruckAdapter(foodTruckList);
                recyclerView.setAdapter(adapter);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new ItemDecorator(0, 0, 0, 10));

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Error" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(this).add(getTrucks);
    }
}
