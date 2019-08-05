package com.tsafack.alc40phase2challenge;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.tsafack.alc40phase2challenge.administration.AdminActivity;
import com.tsafack.alc40phase2challenge.api.UserHelper;
import com.tsafack.alc40phase2challenge.base.BaseActivity;
import com.tsafack.alc40phase2challenge.entities.User;

import java.util.Arrays;

public class LoginActivity extends BaseActivity {

    private static final int RC_SIGN_IN = 123;
    private ConstraintLayout mylayout;
    private Button sinIngButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mylayout = findViewById(R.id.activitymain_id);
        sinIngButton = findViewById(R.id.singIn_button);

        sinIngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSign();
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startSign();
        } else {
            startNextActivity();
        }
    }

    private void startSign() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(),
                                        new AuthUI.IdpConfig.GoogleBuilder().build())
                        )
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_launcher_background)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        this.responceprocessing(requestCode, resultCode, data);
    }

    private void responceprocessing(int requestCode, int resultCode, Intent data) {
        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.mylayout, "Connexion Ok");

                User myUser = new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), "");

                UserHelper.createUser(myUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startNextActivity();
                    }
                }).addOnFailureListener(this.onFailureListener());
            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.mylayout, "authentication canceled");
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackBar(this.mylayout, "no internet connection");
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    showSnackBar(this.mylayout, "error_unknown_error");
                }
            }
        }
    }


    private void startNextActivity() {
        startActivity(new Intent(getApplicationContext(), FetchingHolidayDealActivity.class));
        finish();
    }

    private void showSnackBar(ConstraintLayout mylayout, String message) {
        Snackbar.make(mylayout, message, Snackbar.LENGTH_SHORT).show();
    }
}
