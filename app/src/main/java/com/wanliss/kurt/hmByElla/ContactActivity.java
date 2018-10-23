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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ContactActivity extends AppCompatActivity implements ContactAdapter.ListItemClickListener, GlobalLogin.LoginListener {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;
    private SwipeController swipeController = null;
    private RecyclerView mContactRecyclerView;
    private ContactAdapter mContactRecyclerViewAdapter;
    private List<ClientContactDTO> mAllClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location));
    }

    @Override
    public void onListItemClick(ClientContactDTO clickedClient) {
        Context context = ContactActivity.this;
        Class destinationActivity = DisplayClientContactActivity.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clickedClient", clickedClient);
        startChildActivityIntent.putExtras(bundle);
        startActivity(startChildActivityIntent);
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {
        if (admin == GlobalLogin.dataSet.SET_TRUE) {
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ContactActivity.this, AddClientContactActivity.class);
                    ContactActivity.this.startActivity(intent);
                }
            });

            mAllClients = new ArrayList<>();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            mContactRecyclerView = findViewById(R.id.contact_list);
            mContactRecyclerView.setHasFixedSize(true);
            mContactRecyclerView.setLayoutManager(linearLayoutManager);

            swipeController = new SwipeController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
                    validateDeleteDialog(mAllClients.get(position));
                }

                @Override
                public void onLeftClicked(int position) {
                    Context context = ContactActivity.this;
                    Class destinationActivity = AddClientContactActivity.class;
                    Intent startChildActivityIntent = new Intent(context, destinationActivity);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("clickedClient", mAllClients.get(position));
                    startChildActivityIntent.putExtras(bundle);
                    startActivity(startChildActivityIntent);
                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(mContactRecyclerView);

            mContactRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });

            mClientContactInfo.child(getString(R.string.main_user_db_contact_location))
                    .orderByChild("lastName").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    getAllClients(dataSnapshot);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    clientDeleted(dataSnapshot);
                    getAllClients(dataSnapshot);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    clientDeleted(dataSnapshot);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // showErrorMessage();
                }
            });


        } else {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }

    private void clientDeleted(DataSnapshot dataSnapshot) {
        ClientContactDTO client = dataSnapshot.getValue(ClientContactDTO.class);
        for (int i = 0; i < mAllClients.size(); i++) {
            if (mAllClients.get(i).getId().equals(Objects.requireNonNull(client).getId())) {
                mAllClients.remove(i);
            }
        }
        mContactRecyclerViewAdapter.notifyDataSetChanged();
        mContactRecyclerViewAdapter = new ContactAdapter(ContactActivity.this, mAllClients);
        mContactRecyclerView.setAdapter(mContactRecyclerViewAdapter);
    }

    private void getAllClients(DataSnapshot dataSnapshot) {
        ClientContactDTO client = dataSnapshot.getValue(ClientContactDTO.class);
        mAllClients.add(client);
        mContactRecyclerViewAdapter = new ContactAdapter(ContactActivity.this, mAllClients);
        mContactRecyclerView.setAdapter(mContactRecyclerViewAdapter);
    }

    private void deleteClient(ClientContactDTO client) {
        String clientId = client.getId();
        mClientContactInfo.child(getString(R.string.main_user_db_contact_location)).child(clientId).removeValue();
        mClientContactInfo.child(getString(R.string.main_user_db_measurement_location)).child(clientId).removeValue();
        Snackbar.make(mContactRecyclerView, " Deleted ", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void validateDeleteDialog(final ClientContactDTO client) {
        String full_name = client.getFirstName() + " " + client.getLastName();
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
}