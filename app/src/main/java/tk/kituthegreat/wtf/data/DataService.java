package tk.kituthegreat.wtf.data;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tk.kituthegreat.wtf.activities.FoodTrucksListActivity;
import tk.kituthegreat.wtf.activities.ReviewsActivity;
import tk.kituthegreat.wtf.constants.Constants;
import tk.kituthegreat.wtf.model.FoodTruck;
import tk.kituthegreat.wtf.model.FoodTruckReview;

import static com.android.volley.Request.Method.GET;

public class DataService {
    private static DataService instance = new DataService();

    public static DataService getInstance() {
        return instance;
    }

    private DataService() {
    }

    // Request all the FoodTrucks

    public ArrayList<FoodTruck> downloadAllFoodTrucks(Context context, final FoodTrucksListActivity.TrucksDownloaded listener) {
        String url = Constants.GET_FOOD_TRUCKS;
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

                listener.success(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Error" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(context).add(getTrucks);
        return foodTruckList;

    }

    // Request all the FoodTrucks reviews

    public ArrayList<FoodTruckReview> downloadReviews(Context context, FoodTruck foodTruck, final ReviewsActivity.ReviewInterface listener) {
        String url = Constants.GET_REVIEWS + foodTruck.getId();
        //String url = "http://10.0.2.2:3005/v1/foodtruck/reviews/truckID";

        final ArrayList<FoodTruckReview> reviewsList = new ArrayList<>();
        System.out.println("Testing");

        final JsonArrayRequest getReviews = new JsonArrayRequest(GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println(response.toString());

                try {
                    JSONArray reviews = response;
                    for (int x = 0; x < reviews.length(); x++) {
                        JSONObject review = reviews.getJSONObject(x);
                        String title = review.getString("title");
                        String id = review.getString("_id");
                        String text = review.getString("text");

                        FoodTruckReview newFoodTruckReview = new FoodTruckReview(id, title, text);
                        reviewsList.add(newFoodTruckReview);
                    }
                } catch (JSONException e) {
                    Log.v("JSON", "EXC" + e.getLocalizedMessage());
                }

                listener.success(true);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("API", "Error" + error.getLocalizedMessage());
            }
        });
        Volley.newRequestQueue(context).add(getReviews);
        return reviewsList;

    }
}
