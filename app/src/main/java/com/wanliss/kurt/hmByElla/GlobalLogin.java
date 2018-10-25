package com.wanliss.kurt.hmByElla;
//udacity01


import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class GlobalLogin extends AppCompatActivity {
    private static final String TAG = GlobalLogin.class.getSimpleName();
    private static dataSet admin = dataSet.NOT_SET;
    static private LoginListener mOnLoginListener;

    public GlobalLogin() {
        admin = dataSet.NOT_SET;
    }

    public static dataSet isAdmin() {
        return admin;
    }

    public static void setAdmin(dataSet dadmin) {
        admin = dadmin;
    }

    static void initialize_drawer(final Context callingContext) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        Menu mDrawerMenu;
        final MenuItem mSignIn;
        mOnLoginListener = (LoginListener) callingContext; //listener;
        final View rootView = ((Activity) callingContext).getWindow().getDecorView();
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        final DrawerLayout drawer = rootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                ((Activity) callingContext), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = rootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavDrawer(callingContext));
        mDrawerMenu = navigationView.getMenu();
        final Menu nav_Menu = navigationView.getMenu();
        mSignIn = mDrawerMenu.findItem(R.id.nav_sign_in);
        if (mUser != null) {

            FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
            DatabaseReference users = mDatabase.getReference(callingContext.getString(R.string.globallogin_db_location) ).child(mUser.getUid());
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange( DataSnapshot snapshot) {
                    if (snapshot.getValue() == null){
                        admin = dataSet.SET_FALSE;
                        mSignIn.setTitle(callingContext.getString(R.string.globallogin_sign_out)  + mUser.getDisplayName());
                        nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                        nav_Menu.findItem(R.id.contact).setVisible(false);
                    } else if (snapshot.getValue().equals(callingContext.getString(R.string.globallogin_snapshot_true) )) {
                        admin = dataSet.SET_TRUE;
                        mSignIn.setTitle(callingContext.getString(R.string.globallogin_sign_out)  + mUser.getDisplayName());
                        nav_Menu.findItem(R.id.nav_storage).setVisible(true);
                        nav_Menu.findItem(R.id.contact).setVisible(true);
                    } else {
                        admin = dataSet.SET_FALSE;
                        mSignIn.setTitle(callingContext.getString(R.string.globallogin_sign_out)  + mUser.getDisplayName());
                        nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                        nav_Menu.findItem(R.id.contact).setVisible(false);
                    }
                    mOnLoginListener.onCheckedLogIn(admin);
                }

                @Override
                public void onCancelled( DatabaseError databaseError) {
                    admin = dataSet.NOT_SET;
                    mSignIn.setTitle(callingContext.getString(R.string.globallogin_sign_out)  + mUser.getDisplayName());
                    nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                    nav_Menu.findItem(R.id.contact).setVisible(false);
                    Log.w(TAG, callingContext.getString(R.string.globallogin_error_msg), databaseError.toException());

                }
            });
        } else {
            mSignIn.setTitle(callingContext.getString(R.string.globallogin_sign_in) );
            nav_Menu.findItem(R.id.nav_storage).setVisible(false);
            nav_Menu.findItem(R.id.contact).setVisible(false);
        }
    }

    enum dataSet {
        NOT_SET,
        SET_TRUE,
        SET_FALSE
    }

    public interface LoginListener {
        void onCheckedLogIn(dataSet admin);
    }
}