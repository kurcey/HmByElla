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
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

public class ClientContact extends Fragment {
    private View mView;
    callOtherFragment mCallback;

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference mClientContactInfo = mDatabase.getReference("users/" + mUser.getUid());
    private ClientContactDTO mContact;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (callOtherFragment) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = inflater.inflate(R.layout.client_contact_frag, container, false);

        Button maleBtn = mView.findViewById(R.id.male_button);
        maleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (writeClientContact())
                            mCallback.selectFragment("male", mContact);
                    }
                });

        Button femaleBtn = mView.findViewById(R.id.female_button);
        femaleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        if (writeClientContact())
                            mCallback.selectFragment("female", mContact);
                    }
                });

        return mView;
    }

    public boolean writeClientContact() {
        boolean result;
        ClientContactDTO contact = readContact();
        result = ValidateInput(contact.getFirstName(), contact.getLastName());
        if (result) {
            mClientContactInfo.child("contacts/" + contact.getLastName()
                    + "_" + contact.getMiddleName()
                    + "_" + contact.getFirstName()).setValue(contact);
            Snackbar.make(mView, "Saving Contact", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return result;
    }

    private boolean ValidateInput(String fName, String lName) {
        boolean result = false;
        if (fName.equals("") || lName.equals("")) {
            Snackbar.make(mView, "Please enter first and last Name to Save.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else result = true;

        return result;
    }

    private ClientContactDTO readContact() {

        EditText firstName = mView.findViewById(R.id.first_name);
        EditText lastName = mView.findViewById(R.id.last_name);
        EditText middleName = mView.findViewById(R.id.middle_name);
        EditText emailAddress = mView.findViewById(R.id.email_address);
        EditText phoneNumber = mView.findViewById(R.id.phone_number);
        EditText address = mView.findViewById(R.id.address);

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String mName = middleName.getText().toString();
        String email = emailAddress.getText().toString();
        String phone = phoneNumber.getText().toString();
        String addr = address.getText().toString();

        mContact = new ClientContactDTO();

        mContact.setFirstName(fName);
        mContact.setLastName(lName);
        mContact.setMiddleName(mName);
        mContact.setEmailAddress(email);
        mContact.setPhoneNumber(phone);
        mContact.setAddress(addr);

        return mContact;
    }

    public interface callOtherFragment {
        void selectFragment(String otherFragment, ClientContactDTO contactInfo);
    }
}