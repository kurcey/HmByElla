/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;
import com.wanliss.kurt.hmByElla.DTO.ClientfMeasureDTO;

public class ClientFMeasure extends Fragment {
    private View mView;
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference mClientContactInfo = mDatabase.getReference("users/" + mUser.getUid());
    private ClientContactDTO mClientInfo;

    @Override
    public void onStop() {
        super.onStop();
        mClientContactInfo.child("measurement/" + mClientInfo.getLastName()
                + "_" + mClientInfo.getMiddleName()
                + "_" + mClientInfo.getFirstName() + "/femaleMeasurement").setValue(readMeasurements());

        Snackbar.make(mView, "Saving Measurements", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.client_f_measure_frag, container, false);
        Bundle arguments = getArguments();
        mClientInfo = (ClientContactDTO) arguments.getSerializable("clientInfo");

        return mView;
    }


    private ClientfMeasureDTO readMeasurements() {

        EditText neck = mView.findViewById(R.id.f_neck);
        EditText frontWidth = mView.findViewById(R.id.f_front_width);
        EditText highBust = mView.findViewById(R.id.f_high_bust);
        EditText bust = mView.findViewById(R.id.f_bust);
        EditText fullFrontWidth = mView.findViewById(R.id.f_full_front_width);
        EditText waist = mView.findViewById(R.id.f_waist);
        EditText highHip = mView.findViewById(R.id.f_high_hips);
        EditText hips = mView.findViewById(R.id.f_hips);
        EditText thigh = mView.findViewById(R.id.f_thigh);
        EditText knee = mView.findViewById(R.id.f_knee);
        EditText calf = mView.findViewById(R.id.f_calf);
        EditText ankle = mView.findViewById(R.id.f_ankle);
        EditText lengthNeckWaist = mView.findViewById(R.id.f_rise);
        EditText lengthWaistFloor = mView.findViewById(R.id.f_length);
        EditText inseam = mView.findViewById(R.id.f_inseam);
        EditText crotchDepth = mView.findViewById(R.id.f_crotch);
        EditText armscye = mView.findViewById(R.id.f_armscye_depth);
        EditText shoulderElbow = mView.findViewById(R.id.f_shoulder_elbow);
        EditText elbowWrist = mView.findViewById(R.id.f_elbow_wrist);
        EditText bicep = mView.findViewById(R.id.f_bicep);
        EditText wrist = mView.findViewById(R.id.f_wrist);
        EditText backWaistLength = mView.findViewById(R.id.f_back_waist_length);
        EditText crossBackWidth = mView.findViewById(R.id.f_cross_back_width);
        EditText fullBackWidth = mView.findViewById(R.id.f_full_back_width);
        EditText waistKnee = mView.findViewById(R.id.f_waist_knee);
        EditText waistCalf = mView.findViewById(R.id.f_waist_calf);
        EditText waistAnkle = mView.findViewById(R.id.f_waist_ankle);
        EditText waistFloor = mView.findViewById(R.id.f_waist_floor);

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


}