package com.tsafack.alc40phase2challenge.api;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class StorageHelper {

    private static StorageReference getReference() {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        return mStorage;
    }

    public static UploadTask uploadImage(Uri imageUri) {
        StorageReference riversRef = getReference().child("images/" + imageUri.getLastPathSegment());
        return riversRef.putFile(imageUri);
    }

  /*  public static FileDownloadTask downloadImage(){
        return getReference().getFile()
    }*/
}
