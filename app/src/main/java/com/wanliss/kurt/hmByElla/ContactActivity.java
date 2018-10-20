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
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements ContactAdapter.ListItemClickListener
//        ClientContact.callOtherFragment
{
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference mClientContactInfo = mDatabase.getReference("users/GlJk1ty08Bgtvdbtk6Akyytka983/contacts");
    SwipeController swipeController = null;
    private ClientContact mClientContactFragment;
    private ClientFMeasure mClientFMeasureFragment;
    private ClientMMeasure mClientMMeasureFragment;
    private FragmentManager mTransaction;
    private String mClientFragment;
    private String mFemaleFragment;
    private String mMaleFragment;
    private ClientContactDTO mContact;
    private RecyclerView mContactRecyclerView;
    private ContactAdapter mContactRecyclerViewAdapter;
    private List<ClientContactDTO> mAllClients;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalLogin.isAdmin() == GlobalLogin.dataSet.SET_TRUE) {
            setContentView(R.layout.contact_activity);
            // setTheme(R.style.AppTheme);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            GlobalLogin.initilize_drawer(this);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            mAllClients = new ArrayList<>();

            mLinearLayoutManager = new LinearLayoutManager(this);
            mContactRecyclerView = findViewById(R.id.contact_list);
            mContactRecyclerView.setHasFixedSize(true);
            mContactRecyclerView.setLayoutManager(mLinearLayoutManager);


            swipeController = new SwipeController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
/*
                mContactRecyclerViewAdapter.clients.remove(position);
                mContactRecyclerViewAdapter.notifyItemRemoved(position);
                mContactRecyclerViewAdapter.notifyItemRangeChanged(position, mContactRecyclerViewAdapter.getItemCount());
                */

                }
            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(mContactRecyclerView);

            mContactRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });

            mClientContactInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ClientContactDTO store = dataSnapshot.getValue(ClientContactDTO.class);
                        mAllClients.add(store);
                    }
                    mContactRecyclerViewAdapter = new ContactAdapter(ContactActivity.this, mAllClients);
                    mContactRecyclerView.setAdapter(mContactRecyclerViewAdapter);
                    //progressDialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // showErrorMessage();

                }
            });
 /*


        mClientFragment = getResources().getString(R.string.client_fragment);
        mFemaleFragment = getResources().getString(R.string.female_fragment);
        mMaleFragment = getResources().getString(R.string.male_fragment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTransaction = getSupportFragmentManager();

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }
            mClientContactFragment = new ClientContact();
            mClientContactFragment.setArguments(getIntent().getExtras());

            mTransaction
                    .beginTransaction()
                    .add(R.id.fragment_container, mClientContactFragment, mClientFragment)
                    .addToBackStack(null)
                    .commit();

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.contactFab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment currentFragment = mTransaction.findFragmentById(R.id.fragment_container);

                    if (currentFragment.getTag() != null)
                        switch (currentFragment.getTag()) {
                        case "client_fragment":
                            ((ClientContact) currentFragment).writeClientContact();
                            break;
                        case "female_fragment":
                            break;
                        case "male_fragment":
                            break;

                    }
                }
            });
        }

    }


    @Override
    public void selectFragment(String otherFragment, ClientContactDTO contactInfo) {
        Bundle arguments = new Bundle();
        arguments.putSerializable("clientInfo", contactInfo);

        switch (otherFragment) {
            case "female":
                mClientFMeasureFragment = new ClientFMeasure();
                mClientFMeasureFragment.setArguments(arguments);
                mTransaction
                        .beginTransaction()
                        .replace(R.id.fragment_container, mClientFMeasureFragment, mFemaleFragment)
                        .addToBackStack(null)
                        .commit();
                break;

            case "male":
                mClientMMeasureFragment = new ClientMMeasure();
                mClientMMeasureFragment.setArguments(arguments);
                mTransaction
                        .beginTransaction()
                        .replace(R.id.fragment_container, mClientMMeasureFragment, mMaleFragment)
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }
*/

        } else {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }

    @Override
    public void onListItemClick(ClientContactDTO clickedMovie) {
        Context context = ContactActivity.this;
        /*
        Class destinationActivity = ImagesActivity.class;

        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clickedImage", clickedImage);
        startChildActivityIntent.putExtras(bundle);
        startActivity(startChildActivityIntent);
        */
    }

}