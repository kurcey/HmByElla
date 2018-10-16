/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.sewingmate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

class NavDrawer implements NavigationView.OnNavigationItemSelectedListener {
    private final Context mContext;
    private final Activity mActivity;


    public NavDrawer(Context currentContext) {
        this.mContext = currentContext;
        this.mActivity = (Activity) currentContext;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        Snackbar.make(this.mActivity.findViewById(R.id.drawer_layout), item.getTitle() + " " + Integer.toString(id),
                Snackbar.LENGTH_SHORT)
                .show();

        if (id == R.id.contact) {
            Intent intent = new Intent(this.mActivity, ContactActivity.class);
            this.mActivity.startActivity(intent);
            return true;

        } else if (id == R.id.nav_sign_in) {
            if(item.getTitle().equals("sign in")) {
                Intent intent = new Intent(this.mActivity, Login.class);
                this.mActivity.startActivity(intent);
            }
            else signOut(item);
            return true;
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(this.mActivity, MainActivity.class);
            this.mActivity.startActivity(intent);
            return true;
        } else if (id == R.id.nav_storage) {
            Intent intent = new Intent(this.mActivity, StorageActivity.class);
            this.mActivity.startActivity(intent);
            return true;

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = this.mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut(MenuItem item) {
        item.setTitle("sign in");
        AuthUI.getInstance()
                .signOut(this.mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

}
