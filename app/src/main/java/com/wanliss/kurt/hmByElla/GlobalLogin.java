package com.wanliss.kurt.hmByElla;


import android.app.Activity;
import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class GlobalLogin extends AppCompatActivity {
    private static final GlobalLogin ourInstance = new GlobalLogin();
    private static dataSet admin;

    public GlobalLogin() {
        admin = dataSet.NOT_SET;
    }

    public static GlobalLogin getInstance() {
        return ourInstance;
    }

    public static dataSet isAdmin() {
        return admin;
    }

    public void setAdmin(dataSet admin) {
        this.admin = admin;
    }

    protected static void initilize_drawer(Context callingContext) {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        Menu mDrawerMenu;
        final MenuItem mSignIn;

        View rootView = ((Activity) callingContext).getWindow().getDecorView();
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        final DrawerLayout drawer = rootView.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                ((Activity) callingContext), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = rootView.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavDrawer(callingContext));
        mDrawerMenu = navigationView.getMenu();

        mSignIn = mDrawerMenu.findItem(R.id.nav_sign_in);
        if (mUser != null) {

        final Menu nav_Menu = navigationView.getMenu();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = mDatabase.getReference("admin").child(mUser.getUid());
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().equals("true")) {
                    admin = dataSet.SET_TRUE;
                    mSignIn.setTitle("sign out " + mUser.getDisplayName());
                    nav_Menu.findItem(R.id.nav_storage).setVisible(true);
                    nav_Menu.findItem(R.id.contact).setVisible(true);
                } else {
                    admin = dataSet.SET_FALSE;
                    mSignIn.setTitle("sign out " + mUser.getDisplayName());
                    nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                    nav_Menu.findItem(R.id.contact).setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                admin = dataSet.NOT_SET;
                mSignIn.setTitle("sign out " + mUser.getDisplayName());
                nav_Menu.findItem(R.id.nav_storage).setVisible(false);
                nav_Menu.findItem(R.id.contact).setVisible(false);
            }
        });
        }
        else
            mSignIn.setTitle("sign in");

    }

    //public class getAdmin implements adminResults
    protected static void isAdmin(String userId)
    {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference users = mDatabase.getReference("admin").child(userId);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue().equals("true")) {
                    admin = dataSet.SET_TRUE;

                } else {
                    admin = dataSet.SET_FALSE;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                admin = dataSet.NOT_SET;
            }
        });
    }



    enum dataSet {
        NOT_SET,
        SET_TRUE,
        SET_FALSE
    }
}
