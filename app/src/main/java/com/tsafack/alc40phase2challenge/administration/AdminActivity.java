package com.tsafack.alc40phase2challenge.administration;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tsafack.alc40phase2challenge.FetchingHolidayDealActivity;
import com.tsafack.alc40phase2challenge.R;
import com.tsafack.alc40phase2challenge.api.HolidayDealHelper;
import com.tsafack.alc40phase2challenge.base.BaseActivity;
import com.tsafack.alc40phase2challenge.entities.HolidayDeal;

import java.util.Objects;

public class AdminActivity extends BaseActivity {

    private static final int RC_SELECT_IMAGE = 2;
    EditText edName, edPprice, edDescription;
    Button selectImage;
    ImageView Dealimage;

    private String urlImage;
    private final int REQUESTrEADiMAGE = 10;
    private FirebaseStorage firebaseStorage;


    private String[] listOfpermission = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private String[] arrayofExtentions = {"image/jpeg", "image/png"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        edName = findViewById(R.id.id_name);
        edPprice = findViewById(R.id.id_amount);
        edDescription = findViewById(R.id.id_description);

        selectImage = findViewById(R.id.id_btn_select_image);
        Dealimage = findViewById(R.id.id_image);
        firebaseStorage = FirebaseStorage.getInstance();

        selectImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(
                        AdminActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                ) {

                    ActivityCompat.requestPermissions(
                            AdminActivity.this, listOfpermission,
                            REQUESTrEADiMAGE
                    );
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*")
                            .setAction(Intent.ACTION_GET_CONTENT)
                            .putExtra(Intent.EXTRA_MIME_TYPES, arrayofExtentions);
                    startActivityForResult(Intent.createChooser(intent, "Selectionnez une image"), RC_SELECT_IMAGE);
                }
            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.id_menu_save:
                saveData();
                return true;
            case R.id.id_menu_list_all:
                startActivity(new Intent(getApplicationContext(), FetchingHolidayDealActivity.class));
                return true;
            default:
                return true;
        }
    }

    private static String TAG = AdminActivity.class.getName();


    //TODO corriger le problème de l'upload des images
    private void saveData() {
        if (!edPprice.getText().toString().equals("") && !edName.getText().toString().equals("")
                && !edDescription.getText().toString().equals("")) {

            final HolidayDeal myHolidayDeal = new HolidayDeal(edName.getText().toString(),
                    Double.parseDouble(edPprice.getText().toString().trim()), edDescription.getText().toString(), urlImage);

            selectedImagePathUri = null; // cette ligne de code est à supprimer
            if (selectedImagePathUri != null) {
                UploadTask uploadTask = getStorageReference().putFile(selectedImagePathUri);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }

                        // Continue with the task to get the download URL
                        return getStorageReference().getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.i(TAG, "URL: " + downloadUri.getPath());
                            Toast.makeText(getApplicationContext(), "URL: " + downloadUri.getPath(), Toast.LENGTH_LONG).show();

                            HolidayDeal myHoliday = new HolidayDeal(edName.getText().toString(), Double.parseDouble(edPprice.getText().toString().trim()),
                                    edDescription.getText().toString(), downloadUri.getPath());
                            HolidayDealHelper.createHolidayDeal(myHolidayDeal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    LinearLayout parentLayout = findViewById(R.id.id_admin_activity);
                                    Snackbar.make(parentLayout, "New holidayDeal has to created with image", Snackbar.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(AdminActivity.this.onFailureListener());

                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                });
            } else {
                HolidayDealHelper.createHolidayDeal(myHolidayDeal).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        LinearLayout parentLayout = findViewById(R.id.id_admin_activity);
                        Snackbar.make(parentLayout, "New holidayDeal has created ", Snackbar.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(this.onFailureListener());

            }
        } else {
            edPprice.setError("Require...");
            edName.setError("Require...");
            edDescription.setError("Require...");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    public void uploadFile(final Uri fileUri) {
        ;

        final String toFilePath = fileUri.getLastPathSegment();

        StorageReference storageRef = firebaseStorage.getReference();
        StorageReference uploadeRef = storageRef.child(toFilePath);

        uploadeRef.putFile(fileUri).addOnFailureListener(new OnFailureListener() {
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Failed to upload file to cloud storage: " + exception.toString());
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUESTrEADiMAGE) {
            Intent intent = new Intent();
            intent.setType("image/*")
                    .setAction(Intent.ACTION_GET_CONTENT)
                    .putExtra(Intent.EXTRA_MIME_TYPES, arrayofExtentions);
            startActivityForResult(Intent.createChooser(intent, "Selectionnez une image"), RC_SELECT_IMAGE);
        }
    }

    Uri selectedImagePathUri = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
                data != null && data.getData() != null
        ) {
            selectedImagePathUri = data.getData();
            Dealimage.setImageURI(selectedImagePathUri);
        }
    }

    private StorageReference mStorageRef;

    private static StorageReference getStorageReference() {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        return mStorage;
    }
}
