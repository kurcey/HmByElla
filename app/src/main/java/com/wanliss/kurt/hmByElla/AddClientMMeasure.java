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
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.ClientMMeasureDTO;

public class AddClientMMeasure extends AppCompatActivity implements GlobalLogin.LoginListener {
    private static final String TAG = AddClientMMeasure.class.getSimpleName();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;

    private EditText chest = null;
    private EditText waist = null;
    private EditText hips = null;
    private EditText rise = null;
    private EditText length = null;
    private EditText inseam = null;
    private EditText outseam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_male_measure_activity);
        setTheme(R.style.AppTheme);
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
        String key = i.getStringExtra(getString(R.string.add_mmeasure_intent_key));
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location))
                .child(getString(R.string.main_user_db_measurement_location))
                .child(key)
                .child(getString(R.string.add_mmeasure_db_location));
        readMaleMeasureFromCloud();
    }

    @Override
    public void onStop() {
        super.onStop();
        writeMaleMeasureToCloud();
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
                Log.w(TAG, getString(R.string.add_mmeasure_error_msg), databaseError.toException());
            }
        };
        mClientContactInfo.addListenerForSingleValueEvent(measurementListener);
    }

    private void writeMaleMeasureToCloud() {
        ClientMMeasureDTO contact = readMaleMeasurementsFromForm();
        NestedScrollView ContactInfo = this.findViewById(R.id.m_measure_frame);

        mClientContactInfo.setValue(contact);

        Snackbar.make(ContactInfo, getString(R.string.add_mmeasure_saving_msg), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.add_mmeasure_saving_contact_action), null).show();
    }

    private ClientMMeasureDTO readMaleMeasurementsFromForm() {
        String sChest = chest.getText().toString();
        String sWaist = waist.getText().toString();
        String sHips = hips.getText().toString();
        String sRise = rise.getText().toString();
        String sLength = length.getText().toString();
        String sInseam = inseam.getText().toString();
        String sOutseam = outseam.getText().toString();

        return new ClientMMeasureDTO(sChest, sWaist, sHips, sRise, sLength, sInseam, sOutseam);
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
        if (admin != GlobalLogin.dataSet.SET_TRUE) {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }
}