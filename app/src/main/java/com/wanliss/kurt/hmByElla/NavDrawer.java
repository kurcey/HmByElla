/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

class NavDrawer implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = NavDrawer.class.getSimpleName();
    private final Context mContext;
    private final Activity mActivity;
    private final Menu nav_Menu;


    public NavDrawer(Context currentContext) {

        NavigationView navigationView;
        this.mContext = currentContext;
        this.mActivity = (Activity) currentContext;

        navigationView = this.mActivity.findViewById(R.id.nav_view);
        nav_Menu = navigationView.getMenu();

        switch (GlobalLogin.isAdmin()) {
            case SET_TRUE:
                nav_Menu.findItem(R.id.nav_storage).setVisible(true);
                nav_Menu.findItem(R.id.contact).setVisible(true);
                break;
            case NOT_SET:
            case SET_FALSE:
            default:
                nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                nav_Menu.findItem(R.id.contact).setVisible(false);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;

        switch (id) {

            case R.id.nav_gallery:
                intent = new Intent(this.mActivity, GalleryActivity.class);
                this.mActivity.startActivity(intent);
                break;

            case R.id.nav_about_us:
                intent = new Intent(this.mActivity, AboutUsActivity.class);
                this.mActivity.startActivity(intent);
                break;

            case R.id.contact:
                intent = new Intent(this.mActivity, ContactActivity.class);
                this.mActivity.startActivity(intent);
                break;

            case R.id.nav_storage:
                intent = new Intent(this.mActivity, UploadImageActivity.class);
                this.mActivity.startActivity(intent);
                break;

            case R.id.nav_share:
                intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, this.mActivity.getString(R.string.share_text));
                intent.setType(this.mActivity.getString(R.string.share_type));
                this.mActivity.startActivity(intent);
                break;

            case R.id.nav_send:
                sendEmail();
                break;

            case R.id.nav_sign_in:
                if (GlobalLogin.isAdmin() == GlobalLogin.dataSet.NOT_SET) {
                    intent = new Intent(this.mActivity, Login.class);
                    this.mActivity.startActivity(intent);
                } else signOut(item);
                break;

            default:
                Snackbar.make(this.mActivity.findViewById(R.id.drawer_layout), item.getTitle()
                                + this.mActivity.getString(R.string.space_text) + Integer.toString(id),
                        Snackbar.LENGTH_SHORT)
                        .show();
                break;
        }

        DrawerLayout drawer = this.mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut(MenuItem item) {
        GlobalLogin.setAdmin(GlobalLogin.dataSet.NOT_SET);
        item.setTitle(this.mActivity.getString(R.string.sign_in));
        nav_Menu.findItem(R.id.nav_storage).setVisible(false);
        nav_Menu.findItem(R.id.contact).setVisible(false);
        AuthUI.getInstance()
                .signOut(this.mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(mActivity, GalleryActivity.class);
                        mActivity.startActivity(intent);
                    }
                });
    }


    private void sendEmail() {
        Log.i(TAG, this.mActivity.getString(R.string.nav_email_intent));
        String[] TO = {this.mActivity.getString(R.string.nav_email)};
        String[] CC = {this.mActivity.getString(R.string.blank_text)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        //emailIntent.setType(this.mActivity.getString(R.string.nav_email_type));
        emailIntent.setData(Uri.parse(this.mActivity.getString(R.string.nav_email_mailto)));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, this.mActivity.getString(R.string.nav_email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, this.mActivity.getString(R.string.nav_email_message_hint));

        try {
            this.mActivity.startActivity(Intent.createChooser(emailIntent, this.mActivity.getString(R.string.nav_email_intent)));
            // finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this.mActivity, this.mActivity.getString(R.string.toast_no_email_installed), Toast.LENGTH_SHORT).show();
        }
    }
}
