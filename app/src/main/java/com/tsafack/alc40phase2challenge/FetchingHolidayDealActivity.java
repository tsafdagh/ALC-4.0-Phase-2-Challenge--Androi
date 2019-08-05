package com.tsafack.alc40phase2challenge;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsafack.alc40phase2challenge.adapter.HolidayDealRecyclerViewAdapter;
import com.tsafack.alc40phase2challenge.administration.AdminActivity;
import com.tsafack.alc40phase2challenge.api.HolidayDealHelper;
import com.tsafack.alc40phase2challenge.base.BaseActivity;
import com.tsafack.alc40phase2challenge.entities.HolidayDeal;

import java.util.ArrayList;

public class FetchingHolidayDealActivity extends BaseActivity {


    RecyclerView holidaydealReceycleView;
    ArrayList<HolidayDeal> holidayDealsList = new ArrayList<>();
    private String TAG = FetchingHolidayDealActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetching_holiday_deal);

        holidaydealReceycleView = (RecyclerView) findViewById(R.id.id_receycle_view);
        LinearLayoutManager recyclerLayoutManager =
                new LinearLayoutManager(getApplicationContext());
        holidaydealReceycleView.setLayoutManager(recyclerLayoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(holidaydealReceycleView.getContext(),
                        recyclerLayoutManager.getOrientation());
        holidaydealReceycleView.addItemDecoration(dividerItemDecoration);
        loadData();
    }

    private void loadData() {
        HolidayDealHelper.getAllHolidayDeal().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        HolidayDeal currentHoliday = document.toObject(HolidayDeal.class);
                        holidayDealsList.add(currentHoliday);
                    }
                    HolidayDealRecyclerViewAdapter recyclerViewAdapter = new
                            HolidayDealRecyclerViewAdapter(
                            getApplicationContext(), holidayDealsList);
                    holidaydealReceycleView.setAdapter(recyclerViewAdapter);

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    Toast.makeText(FetchingHolidayDealActivity.this, "Error getting documents: ", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(this.onFailureListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_activity_feiching_all,  menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.id_menu_create_holiday){
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));

        }
        return true;
    }
}
