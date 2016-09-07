package com.gdg.hanoi.demo;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.gdg.hanoi.demo.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // host: https://codelab2.firebaseio.com/
    private static final String FIREBASE_HOST = "https://codelab2.firebaseio.com/";

    // credentical to authenticate
    private String mEmail = "khacpv@gmail.com";
    private String mPassword = "123456";

    // Firebase reference
    Firebase myFirebaseRef;

    ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // The Firebase library must be initialized once with an Android context.
        // This must happen before any Firebase app reference is created or used.
        Firebase.setAndroidContext(this);

        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mBinding.setEmail(mEmail);
        mBinding.setPassword(mPassword);

        myFirebaseRef = new Firebase(FIREBASE_HOST);

        // Reading data from your Firebase database is accomplished by attaching an event
        // listener and handling the resulting events
        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //prints "Do you have data? You'll love Firebase."
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.out.println(error.getMessage());
            }

        });
    }

    public void onLoginClick(final View button) {
        // Enable authenticate with password on firebase server first
        myFirebaseRef.authWithPassword(mEmail, mPassword,
                new Firebase.AuthResultHandler() {

                    @Override
                    public void onAuthenticated(AuthData authData) {
                        String dataToSend = "Do you have data? You'll love Firebase.";
                        myFirebaseRef.child("message")
                                .setValue(dataToSend);
                        Snackbar.make(button, "login success", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        Snackbar.make(button, firebaseError.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    public void onRegisterClick(final View button) {
        // Firebase provides full support for authenticating users with Email & Password,
        // Facebook, Twitter, GitHub, Google, or your existing authentication system.
        myFirebaseRef.createUser(mEmail, mPassword,   //
                new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        System.out.println("Created account with uid: " + result.get("uid"));
                        Snackbar.make(button, "Created account with uid: " + result.get("uid"),
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Snackbar.make(button, firebaseError.getMessage(), Snackbar.LENGTH_SHORT)
                                .show();
                    }
                });
    }
}
