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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.firebase.ui.auth.AuthUI;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;

    Menu nav_Menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GlobalLogin.initilize_drawer(this);

// Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                // new AuthUI.IdpConfig.EmailBuilder().build(),
                // new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build() //,
                //     new AuthUI.IdpConfig.FacebookBuilder().build(),
                //     new AuthUI.IdpConfig.TwitterBuilder().build()
        );

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            //IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, GalleryActivity.class);
                this.startActivity(intent);
            } else {
                nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                nav_Menu.findItem(R.id.contact).setVisible(false);
            }
        }
    }
}
