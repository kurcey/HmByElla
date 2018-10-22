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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.ClientfMeasureDTO;

public class DisplayClientFMeasureActivity extends AppCompatActivity implements GlobalLogin.LoginListener {

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;

    private TextView neck = null;
    private TextView frontWidth = null;
    private TextView highBust = null;
    private TextView bust = null;
    private TextView fullFrontWidth = null;
    private TextView waist = null;
    private TextView highHip = null;
    private TextView hips = null;
    private TextView thigh = null;
    private TextView knee = null;
    private TextView calf = null;
    private TextView ankle = null;
    private TextView lengthNeckWaist = null;
    private TextView lengthWaistFloor = null;
    private TextView inseam = null;
    private TextView crotchDepth = null;
    private TextView armscye = null;
    private TextView shoulderElbow = null;
    private TextView elbowWrist = null;
    private TextView bicep = null;
    private TextView wrist = null;
    private TextView backWaistLength = null;
    private TextView crossBackWidth = null;
    private TextView fullBackWidth = null;
    private TextView waistKnee = null;
    private TextView waistCalf = null;
    private TextView waistAnkle = null;
    private TextView waistFloor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_female_measure_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location));


        neck = findViewById(R.id.f_neck);
        frontWidth = findViewById(R.id.f_front_width);
        highBust = findViewById(R.id.f_high_bust);
        bust = findViewById(R.id.f_bust);
        fullFrontWidth = findViewById(R.id.f_full_front_width);
        waist = findViewById(R.id.f_waist);
        highHip = findViewById(R.id.f_high_hips);
        hips = findViewById(R.id.f_hips);
        thigh = findViewById(R.id.f_thigh);
        knee = findViewById(R.id.f_knee);
        calf = findViewById(R.id.f_calf);
        ankle = findViewById(R.id.f_ankle);
        lengthNeckWaist = findViewById(R.id.f_rise);
        lengthWaistFloor = findViewById(R.id.f_length);
        inseam = findViewById(R.id.f_inseam);
        crotchDepth = findViewById(R.id.f_crotch);
        armscye = findViewById(R.id.f_armscye_depth);
        shoulderElbow = findViewById(R.id.f_shoulder_elbow);
        elbowWrist = findViewById(R.id.f_elbow_wrist);
        bicep = findViewById(R.id.f_bicep);
        wrist = findViewById(R.id.f_wrist);
        backWaistLength = findViewById(R.id.f_back_waist_length);
        crossBackWidth = findViewById(R.id.f_cross_back_width);
        fullBackWidth = findViewById(R.id.f_full_back_width);
        waistKnee = findViewById(R.id.f_waist_knee);
        waistCalf = findViewById(R.id.f_waist_calf);
        waistAnkle = findViewById(R.id.f_waist_ankle);
        waistFloor = findViewById(R.id.f_waist_floor);

        Intent i = getIntent();
        String key = i.getStringExtra("key");
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location))
                .child(getString(R.string.main_user_db_measurement_location))
                .child(key)
                .child("fMeasure");
        readFemaleMeasureFromCloud();
    }

    private void readFemaleMeasureFromCloud() {
        ValueEventListener measurementListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ClientfMeasureDTO fMeasure = dataSnapshot.getValue(ClientfMeasureDTO.class);
                if (fMeasure != null)
                    fillMeasurements(fMeasure);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mClientContactInfo.addListenerForSingleValueEvent(measurementListener);
    }

    private void fillMeasurements(ClientfMeasureDTO fMeasure) {

        neck.setText(fMeasure.getNeck());
        frontWidth.setText(fMeasure.getFrontWidth());
        highBust.setText(fMeasure.getHighBust());
        bust.setText(fMeasure.getBust());
        fullFrontWidth.setText(fMeasure.getFullFrontWidth());
        waist.setText(fMeasure.getWaist());
        highHip.setText(fMeasure.getHighHip());
        hips.setText(fMeasure.getHips());
        thigh.setText(fMeasure.getThigh());
        knee.setText(fMeasure.getKnee());
        calf.setText(fMeasure.getCalf());
        ankle.setText(fMeasure.getAnkle());
        lengthNeckWaist.setText(fMeasure.getLengthNeckWaist());
        lengthWaistFloor.setText(fMeasure.getLengthWaistFloor());
        inseam.setText(fMeasure.getInseam());
        crotchDepth.setText(fMeasure.getCrotchDepth());
        armscye.setText(fMeasure.getArmscye());
        shoulderElbow.setText(fMeasure.getShoulderElbow());
        elbowWrist.setText(fMeasure.getElbowWrist());
        bicep.setText(fMeasure.getBicep());
        wrist.setText(fMeasure.getWrist());
        backWaistLength.setText(fMeasure.getBackWaistLength());
        crossBackWidth.setText(fMeasure.getCrossBackWidth());
        fullBackWidth.setText(fMeasure.getFullBackWidth());
        waistKnee.setText(fMeasure.getWaistKnee());
        waistCalf.setText(fMeasure.getWaistCalf());
        waistAnkle.setText(fMeasure.getWaistAnkle());
        waistFloor.setText(fMeasure.getWaistFloor());
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {

    }
}