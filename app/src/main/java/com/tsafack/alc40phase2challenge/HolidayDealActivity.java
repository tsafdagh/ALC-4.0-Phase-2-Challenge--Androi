package com.tsafack.alc40phase2challenge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsafack.alc40phase2challenge.api.HolidayDealHelper;
import com.tsafack.alc40phase2challenge.entities.HolidayDeal;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HolidayDealActivity extends AppCompatActivity {


    private List<HolidayDeal> listOfHoliday;
    private final String TAG = "HolidayDealActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_deal);

        listOfHoliday = new ArrayList<>();
        getHolidayDeal();
    }

    private void getHolidayDeal() {
        HolidayDealHelper.getAllHolidayDeal().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        HolidayDeal myHoliday = document.toObject(HolidayDeal.class);
                        listOfHoliday.add(myHoliday);
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
        });
    }
}
