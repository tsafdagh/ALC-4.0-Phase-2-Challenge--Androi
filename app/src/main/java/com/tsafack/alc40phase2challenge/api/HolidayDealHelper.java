package com.tsafack.alc40phase2challenge.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tsafack.alc40phase2challenge.entities.HolidayDeal;

import java.util.HashMap;

public class HolidayDealHelper {

    private static final String CURRENT_COLLECTION = "holidayDeal";

    public static CollectionReference getHolidayDealCollection() {
        return FirebaseFirestore.getInstance().collection(CURRENT_COLLECTION);
    }

    //create
    public static Task<Void> createHolidayDeal(HolidayDeal myHolidayDeal) {
        return HolidayDealHelper.getHolidayDealCollection().document().set(myHolidayDeal);
    }

    // GET ALL
    public static Task<QuerySnapshot> getAllHolidayDeal() {
        return HolidayDealHelper.getHolidayDealCollection().get();
    }

    //GET One
    public static Task<DocumentSnapshot> getSpecifikHolidayDeal(String holidayUid) {
        return HolidayDealHelper.getHolidayDealCollection().document(holidayUid).get();
    }

    //UPDATE
    public static Task<Void> updateHolidayDeal(String holidayDealUid, String newname, Double newAmount, String newDescription, String newImage) {
        HashMap<String, Object> updatedmap = new HashMap<>();

        if (!newname.isEmpty())
            updatedmap.put("name", newname);
        if (!newAmount.toString().isEmpty())
            updatedmap.put("price", newAmount);
        if (!newDescription.isEmpty())
            updatedmap.put("description", newDescription);
        if (!newImage.isEmpty())
            updatedmap.put("urlImage", newImage);

        return HolidayDealHelper.getHolidayDealCollection().document(holidayDealUid).update(updatedmap);
    }

    //DELETE
    public static Task<Void> deleteOnHolidayDeal(String holidayDealUid) {
        return HolidayDealHelper.getHolidayDealCollection().document(holidayDealUid).delete();
    }
}
