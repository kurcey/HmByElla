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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

public class AddClientMMeasure extends AppCompatActivity implements GlobalLogin.LoginListener {
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private View mView;
    private DatabaseReference mClientContactInfo = mDatabase.getReference("users" );
    private String mKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_male_measure_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GlobalLogin.initialize_drawer(this);

        Intent i= getIntent();
        mKey = i.getStringExtra("key");

    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {
        if (admin == GlobalLogin.dataSet.SET_TRUE) {
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });


        } else {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }

/*
    @Override
    public void onStop() {
        super.onStop();
        writefMeasure();
    }


    public String writefMeasure() {
        String key = "";
        ClientfMeasureDTO  contact = readMeasurements();
        NestedScrollView ContactInfo = this.findViewById(R.id.f_measure_frame);

        key = mClientContactInfo.child(mUser.getUid()).child("measurement").child(mKey).child("fMeasure").push().getKey();
        mClientContactInfo.child(mUser.getUid()).child("measurement").child(mKey).child("fMeasure").setValue(contact);
        if (!key.equals("")) {
            Snackbar.make(ContactInfo, "Saving Contact", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(ContactInfo, "Unable to save Please input first and last Name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return key;
    }



    private ClientfMeasureDTO readMeasurements() {

        EditText neck = findViewById(R.id.f_neck);
        EditText frontWidth = findViewById(R.id.f_front_width);
        EditText highBust = findViewById(R.id.f_high_bust);
        EditText bust = findViewById(R.id.f_bust);
        EditText fullFrontWidth = findViewById(R.id.f_full_front_width);
        EditText waist = findViewById(R.id.f_waist);
        EditText highHip = findViewById(R.id.f_high_hips);
        EditText hips = findViewById(R.id.f_hips);
        EditText thigh = findViewById(R.id.f_thigh);
        EditText knee = findViewById(R.id.f_knee);
        EditText calf = findViewById(R.id.f_calf);
        EditText ankle = findViewById(R.id.f_ankle);
        EditText lengthNeckWaist = findViewById(R.id.f_rise);
        EditText lengthWaistFloor = findViewById(R.id.f_length);
        EditText inseam = findViewById(R.id.f_inseam);
        EditText crotchDepth = findViewById(R.id.f_crotch);
        EditText armscye = findViewById(R.id.f_armscye_depth);
        EditText shoulderElbow = findViewById(R.id.f_shoulder_elbow);
        EditText elbowWrist = findViewById(R.id.f_elbow_wrist);
        EditText bicep = findViewById(R.id.f_bicep);
        EditText wrist = findViewById(R.id.f_wrist);
        EditText backWaistLength = findViewById(R.id.f_back_waist_length);
        EditText crossBackWidth = findViewById(R.id.f_cross_back_width);
        EditText fullBackWidth = findViewById(R.id.f_full_back_width);
        EditText waistKnee = findViewById(R.id.f_waist_knee);
        EditText waistCalf = findViewById(R.id.f_waist_calf);
        EditText waistAnkle = findViewById(R.id.f_waist_ankle);
        EditText waistFloor = findViewById(R.id.f_waist_floor);

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

        return new ClientMMeasureDTO(sNeck, sFrontWidth, sHighBust, sBust
                , sFullFrontWidth, sWaist, sHighHip, sHips
                , sThigh, sKnee, sCalf, sAnkle, sLengthNeckWaist
                , sLengthWaistFloor, sInseam, sCrotchDepth, sArmscye
                , sShoulderElbow, sElbowWrist, sBicep, sWrist
                , sBackWaistLength, sCrossBackWidth, sFullBackWidth
                , sWaistKnee, sWaistCalf, sWaistAnkle, sWaistFloor);
    }

*/

}