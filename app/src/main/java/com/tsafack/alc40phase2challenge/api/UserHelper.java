package com.tsafack.alc40phase2challenge.api;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tsafack.alc40phase2challenge.entities.User;

import java.util.HashMap;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // user collection
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    //create user

    public static Task<Void> createUser(User user) {
        return UserHelper.getUsersCollection().document().set(user);
    }

    //get user

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserHelper.getUsersCollection().document(uid).get();
    }

    //update
    public static Task<Void> updateUsername(String name, String imageurl, String phone, String uid) {

        HashMap<String, Object> updatedmap = new HashMap<>();

        if (!name.isEmpty())
            updatedmap.put("name", name);
        if (!phone.isEmpty())
            updatedmap.put("phone", phone);
        if (!imageurl.isEmpty())
            updatedmap.put("imageUrl", imageurl);
        return UserHelper.getUsersCollection().document(uid).update(updatedmap);
    }

    //DELETE user

    public static Task<Void> deleteUser(String uid){
        return UserHelper.getUsersCollection().document(uid).delete();
    }

}
