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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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

public class GalleryActivity extends AppCompatActivity
        implements GalleryAdapter.ListItemClickListener {
    private Menu mDrawerMenu;
    private MenuItem mSignIn;

    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference mStoreDisplayInfo = mDatabase.getReference("images/Gallery");
    private final List<StoreDisplayDTO> mAllStore = new ArrayList<>();

    private RecyclerView mStoreRecyclerView;
    private GalleryAdapter mStoreRecyclerViewAdapter;

    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.cptoolLay);
        final Typeface tf = Typeface.createFromAsset(this.getAssets(), "charmonman_bold.ttf");
       // collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);
        GlobalLogin.initilize_drawer(this);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        mStoreRecyclerView = findViewById(R.id.rv_posters);
        mStoreRecyclerView.setHasFixedSize(true);
        mStoreRecyclerView.setLayoutManager(layoutManager);

        mStoreDisplayInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoreDisplayDTO store = dataSnapshot.getValue(StoreDisplayDTO.class);
                    mAllStore.add(store);
                }
                mStoreRecyclerViewAdapter = new GalleryAdapter(mAllStore, GalleryActivity.this);
                mStoreRecyclerView.setAdapter(mStoreRecyclerViewAdapter);
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showErrorMessage();

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
    public void onListItemClick(StoreDisplayDTO clickedImage) {
        Context context = GalleryActivity.this;
        Class destinationActivity = ImagesActivity.class;

        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clickedImage", clickedImage);
        startChildActivityIntent.putExtras(bundle);
        startActivity(startChildActivityIntent);
    }

    private void showErrorMessage() {
        mStoreRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

}
