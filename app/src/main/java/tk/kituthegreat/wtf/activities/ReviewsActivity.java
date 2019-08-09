package tk.kituthegreat.wtf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.data.DataService;
import tk.kituthegreat.wtf.model.FoodTruck;
import tk.kituthegreat.wtf.model.FoodTruckReview;

public class ReviewsActivity extends AppCompatActivity {

    // Variables
    private FoodTruck foodTruck;
    private ArrayList<FoodTruckReview> reviews = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        foodTruck = getIntent().getParcelableExtra(FoodTrucksListActivity.EXTRA_ITEM_Truck);
        System.out.println(foodTruck.getName());

        ReviewInterface listener = new ReviewInterface() {
            @Override
            public void success(Boolean success) {
                if (success) {
                    System.out.println(reviews);
                }
            }
        };

        reviews = DataService.getInstance().downloadReviews(this, foodTruck, listener);
    }

    public interface ReviewInterface {
        void success(Boolean success);

    }
}
