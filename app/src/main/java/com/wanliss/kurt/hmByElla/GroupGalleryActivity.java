/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.StoreDisplayDTO;

import java.util.ArrayList;
import java.util.List;

public class GroupGalleryActivity extends AppCompatActivity
        implements GalleryAdapter.ListItemClickListener {
    private Menu mDrawerMenu;
    private MenuItem mSignIn;

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private  DatabaseReference mStoreDisplayInfo = null;
    private final List<StoreDisplayDTO> mAllStore = new ArrayList<>();

    private RecyclerView mStoreRecyclerView;
    private GalleryAdapter mStoreRecyclerViewAdapter;

    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        StoreDisplayDTO clickedImage;

        if (extras != null) {
            clickedImage = (StoreDisplayDTO) extras.getSerializable("clickedImage");
            System.out.println(clickedImage.getName());
            System.out.println(clickedImage);
            mStoreDisplayInfo = mDatabase.getReference("images/GroupImages/"+clickedImage.getName());
        }
        setContentView(R.layout.activity_goup_gallery);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavDrawer(this));
        this.mDrawerMenu = navigationView.getMenu();

        mSignIn = mDrawerMenu.findItem(R.id.nav_sign_in);
        if (mUser != null)
            mSignIn.setTitle("sign out " + mUser.getDisplayName());
        else
            mSignIn.setTitle("sign in");


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        mStoreRecyclerView = findViewById(R.id.group_display);
        mStoreRecyclerView.setHasFixedSize(true);
        mStoreRecyclerView.setLayoutManager(layoutManager);

        mStoreDisplayInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoreDisplayDTO store = dataSnapshot.getValue(StoreDisplayDTO.class);
                    mAllStore.add(store);
                }
                mStoreRecyclerViewAdapter = new GalleryAdapter(mAllStore, GroupGalleryActivity.this);
                mStoreRecyclerView.setAdapter(mStoreRecyclerViewAdapter);
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();

            }
        });

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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onListItemClick(StoreDisplayDTO clickedMovie) {
        Context context = GroupGalleryActivity.this;
      /*  Class destinationActivity = DetailsView.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("clickedMovie", clickedMovie);
        startActivity(startChildActivityIntent);
        */
    }

    private void showErrorMessage() {
        mStoreRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}
