package tk.kituthegreat.wtf.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;

import tk.kituthegreat.wtf.R;
import tk.kituthegreat.wtf.adapter.FoodTruckAdapter;
import tk.kituthegreat.wtf.adapter.ReviewAdapter;
import tk.kituthegreat.wtf.data.DataService;
import tk.kituthegreat.wtf.model.FoodTruck;
import tk.kituthegreat.wtf.model.FoodTruckReview;
import tk.kituthegreat.wtf.view.ItemDecorator;

public class ReviewsActivity extends AppCompatActivity {

    // Variables
    private FoodTruck foodTruck;
    private ArrayList<FoodTruckReview> reviews = new ArrayList<>();
    private ReviewAdapter adapter;



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
                    setUpRecycler();
                    if (reviews.size() == 0) {
                        Toast.makeText(getBaseContext(), "No reviews for this Food Truck", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        reviews = DataService.getInstance().downloadReviews(this, foodTruck, listener);
    }

    private void setUpRecycler() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_reviews);
        recyclerView.setHasFixedSize(true);
        adapter = new ReviewAdapter(reviews);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecorator(0, 0, 0, 10));
    }

    public interface ReviewInterface {
        void success(Boolean success);

    }
}
