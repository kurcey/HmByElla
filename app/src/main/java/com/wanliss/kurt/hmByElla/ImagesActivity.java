/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wanliss.kurt.hmByElla.DTO.StoreDisplayDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ImagesActivity extends AppCompatActivity
        implements ImagesAdapter.ListItemClickListener, GlobalLogin.LoginListener {
    private static final String TAG = ContactActivity.class.getSimpleName();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final List<StoreDisplayDTO> mDisplayImage = new ArrayList<>();
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mStoreDisplayInfo = null;
    private StorageReference mGroupStoreImages = null;
    private StorageReference mThumbnailStoreGroupImages = null;
    private RecyclerView mStoreRecyclerView;
    private ImagesAdapter mStoreRecyclerViewAdapter;
    private SwipeController swipeController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);

        CheckInternet iTest = new CheckInternet(this);
        iTest.execute();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        mStoreRecyclerView = findViewById(R.id.group_display);
        mStoreRecyclerView.setHasFixedSize(true);
        mStoreRecyclerView.setLayoutManager(layoutManager);

        Bundle extras = getIntent().getExtras();
        StoreDisplayDTO clickedImage;

        if (extras != null) {
            clickedImage = (StoreDisplayDTO) extras.getSerializable(getString(R.string.images_intent_get_key));
            this.setTitle(Objects.requireNonNull(clickedImage).getName());
            mStoreDisplayInfo = mDatabase.getReference(getString(R.string.thumbnail_GroupImages_db_location)).child(clickedImage.getName());
            mGroupStoreImages = storage.getReference(getString(R.string.storage_group_images)).child(clickedImage.getName());
            mThumbnailStoreGroupImages = storage.getReference(getString(R.string.storage_thumbnails_group_images)).child(clickedImage.getName());
        }

        mStoreDisplayInfo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllImages(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                getAllImages(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                imagesDeleted(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getString(R.string.images_error_msg), databaseError.toException());
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
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {

        if (admin == GlobalLogin.dataSet.SET_TRUE) {
            swipeController = new SwipeController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    validateDeleteDialog(mDisplayImage.get(position));
                }

                @Override
                public void onLeftClicked(int position) {
                    Context context = ImagesActivity.this;
                    Class destinationActivity = UploadImageActivity.class;
                    Intent startChildActivityIntent = new Intent(context, destinationActivity);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(getString(R.string.images_intent_put_key), mDisplayImage.get(position));
                    startChildActivityIntent.putExtras(bundle);
                    startActivity(startChildActivityIntent);
                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(mStoreRecyclerView);

            mStoreRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });
        }
    }

    private void validateDeleteDialog(final StoreDisplayDTO client) {
        String full_name = client.getName();
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.confirm_delete_dialog);
        TextView client_name = dialog.findViewById(R.id.file_name);
        client_name.setText(full_name);

        Button yes = dialog.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
                deleteClient(client);
            }
        });

        Button no = dialog.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });

        dialog.show();
    }


    private void imagesDeleted(DataSnapshot dataSnapshot) {
        StoreDisplayDTO store = dataSnapshot.getValue(StoreDisplayDTO.class);
        for (int i = 0; i < mDisplayImage.size(); i++) {
            if (mDisplayImage.get(i).getPath().equals(Objects.requireNonNull(store).getPath())) {
                mDisplayImage.remove(i);
            }
        }
        mStoreRecyclerViewAdapter.notifyDataSetChanged();
        mStoreRecyclerViewAdapter = new ImagesAdapter(mDisplayImage, ImagesActivity.this);
        mStoreRecyclerView.setAdapter(mStoreRecyclerViewAdapter);

    }

    private void getAllImages(DataSnapshot dataSnapshot) {
        StoreDisplayDTO store = dataSnapshot.getValue(StoreDisplayDTO.class);

        mDisplayImage.add(store);
        mStoreRecyclerViewAdapter = new ImagesAdapter(mDisplayImage, ImagesActivity.this);
        mStoreRecyclerView.setAdapter(mStoreRecyclerViewAdapter);
    }


    private void deleteClient(final StoreDisplayDTO client) {

        mStoreDisplayInfo.child(client.getName()).removeValue();
        mGroupStoreImages.child(client.getName()).delete();
        mThumbnailStoreGroupImages.child(client.getName()).delete();

        Snackbar.make(mStoreRecyclerView, getString(R.string.images_deleted_msg), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.images_action), null).show();
    }
}