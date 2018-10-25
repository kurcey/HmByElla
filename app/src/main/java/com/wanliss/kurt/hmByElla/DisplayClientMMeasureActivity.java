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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.ClientMMeasureDTO;

public class DisplayClientMMeasureActivity extends AppCompatActivity implements GlobalLogin.LoginListener {
    private static final String TAG = DisplayClientMMeasureActivity.class.getSimpleName();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;

    private TextView chest = null;
    private TextView waist = null;
    private TextView hips = null;
    private TextView rise = null;
    private TextView length = null;
    private TextView inseam = null;
    private TextView outseam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_male_measure_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);

        CheckInternet iTest = new CheckInternet(this);
        iTest.execute();

        chest = findViewById(R.id.m_chest);
        waist = findViewById(R.id.m_waist);
        hips = findViewById(R.id.m_hips);
        rise = findViewById(R.id.m_rise);
        length = findViewById(R.id.m_length);
        inseam = findViewById(R.id.m_inseam);
        outseam = findViewById(R.id.m_outseam);

        Intent i = getIntent();
        String key = i.getStringExtra(getString(R.string.display_mmeasure_intent_key));
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location))
                .child(getString(R.string.main_user_db_measurement_location))
                .child(key)
                .child(getString(R.string.display_mmeasure_db_location));
        readMaleMeasureFromCloud();
    }


    private void readMaleMeasureFromCloud() {
        ValueEventListener measurementListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClientMMeasureDTO mMeasure = dataSnapshot.getValue(ClientMMeasureDTO.class);
                if (mMeasure != null)
                    fillMeasurements(mMeasure);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, getString(R.string.display_mmeasure_error_msg), databaseError.toException());
            }
        };
        mClientContactInfo.addListenerForSingleValueEvent(measurementListener);
    }


    private void fillMeasurements(ClientMMeasureDTO mMeasure) {

        chest.setText(mMeasure.getChest());
        waist.setText(mMeasure.getWaist());
        hips.setText(mMeasure.getHips());
        rise.setText(mMeasure.getRise());
        length.setText(mMeasure.getLength());
        inseam.setText(mMeasure.getInseam());
        outseam.setText(mMeasure.getOutseam());
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {

    }
}