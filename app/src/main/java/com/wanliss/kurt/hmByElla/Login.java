/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity implements GlobalLogin.LoginListener {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GlobalLogin.initialize_drawer(this);

// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                 new AuthUI.IdpConfig.EmailBuilder().build(),
                 new AuthUI.IdpConfig.PhoneBuilder().build(),
                 new AuthUI.IdpConfig.GoogleBuilder().build() //,
               //  new AuthUI.IdpConfig.FacebookBuilder().build(),
               //  new AuthUI.IdpConfig.TwitterBuilder().build()
        );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, GalleryActivity.class);
                this.startActivity(intent);
            }
        }
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {
    }
}