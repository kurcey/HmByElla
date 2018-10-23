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
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wanliss.kurt.hmByElla.DTO.ClientfMeasureDTO;

public class AddClientFMeasure extends AppCompatActivity implements GlobalLogin.LoginListener {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;

    private EditText neck = null;
    private EditText frontWidth = null;
    private EditText highBust = null;
    private EditText bust = null;
    private EditText fullFrontWidth = null;
    private EditText waist = null;
    private EditText highHip = null;
    private EditText hips = null;
    private EditText thigh = null;
    private EditText knee = null;
    private EditText calf = null;
    private EditText ankle = null;
    private EditText lengthNeckWaist = null;
    private EditText lengthWaistFloor = null;
    private EditText inseam = null;
    private EditText crotchDepth = null;
    private EditText armscye = null;
    private EditText shoulderElbow = null;
    private EditText elbowWrist = null;
    private EditText bicep = null;
    private EditText wrist = null;
    private EditText backWaistLength = null;
    private EditText crossBackWidth = null;
    private EditText fullBackWidth = null;
    private EditText waistKnee = null;
    private EditText waistCalf = null;
    private EditText waistAnkle = null;
    private EditText waistFloor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_female_measure_activity);
        setTheme(R.style.AppTheme);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);

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

    @Override
    public void onStop() {
        super.onStop();
        writeFemaleMeasureToCloud();
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
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        mClientContactInfo.addListenerForSingleValueEvent(measurementListener);
    }

    private void writeFemaleMeasureToCloud() {
        ClientfMeasureDTO contact = readFemaleMeasurementsFromForm();
        NestedScrollView ContactInfo = this.findViewById(R.id.f_measure_frame);

        mClientContactInfo.setValue(contact);

        Snackbar.make(ContactInfo, "Saving Contact", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    private ClientfMeasureDTO readFemaleMeasurementsFromForm() {

        String sNeck = neck.getText().toString();
        String sFrontWidth = frontWidth.getText().toString();
        String sHighBust = highBust.getText().toString();
        String sBust = bust.getText().toString();
        String sFullFrontWidth = fullFrontWidth.getText().toString();
        String sWaist = waist.getText().toString();
        String sHighHip = highHip.getText().toString();
        String sHips = hips.getText().toString();
        String sThigh = thigh.getText().toString();
        String sKnee = knee.getText().toString();
        String sCalf = calf.getText().toString();
        String sAnkle = ankle.getText().toString();
        String sLengthNeckWaist = lengthNeckWaist.getText().toString();
        String sLengthWaistFloor = lengthWaistFloor.getText().toString();
        String sInseam = inseam.getText().toString();
        String sCrotchDepth = crotchDepth.getText().toString();
        String sArmscye = armscye.getText().toString();
        String sShoulderElbow = shoulderElbow.getText().toString();
        String sElbowWrist = elbowWrist.getText().toString();
        String sBicep = bicep.getText().toString();
        String sWrist = wrist.getText().toString();
        String sBackWaistLength = backWaistLength.getText().toString();
        String sCrossBackWidth = crossBackWidth.getText().toString();
        String sFullBackWidth = fullBackWidth.getText().toString();
        String sWaistKnee = waistKnee.getText().toString();
        String sWaistCalf = waistCalf.getText().toString();
        String sWaistAnkle = waistAnkle.getText().toString();
        String sWaistFloor = waistFloor.getText().toString();

        return new ClientfMeasureDTO(sNeck, sFrontWidth, sHighBust, sBust
                , sFullFrontWidth, sWaist, sHighHip, sHips
                , sThigh, sKnee, sCalf, sAnkle, sLengthNeckWaist
                , sLengthWaistFloor, sInseam, sCrotchDepth, sArmscye
                , sShoulderElbow, sElbowWrist, sBicep, sWrist
                , sBackWaistLength, sCrossBackWidth, sFullBackWidth
                , sWaistKnee, sWaistCalf, sWaistAnkle, sWaistFloor);
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
        if (admin != GlobalLogin.dataSet.SET_TRUE) {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }

}