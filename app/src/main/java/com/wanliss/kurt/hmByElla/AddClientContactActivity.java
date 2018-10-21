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
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

public class AddClientContactActivity extends AppCompatActivity implements GlobalLogin.LoginListener {

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private final DatabaseReference mClientContactInfo = mDatabase.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.add_client_contact_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        GlobalLogin.initialize_drawer(this);

        Button maleBtn = findViewById(R.id.male_button);
        maleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String key = writeClientContact();
                        if (!key.equals("")) {
                            Intent intent = new Intent(AddClientContactActivity.this, AddClientMMeasure.class);
                            intent.putExtra("key", key);
                            AddClientContactActivity.this.startActivity(intent);
                            // mCallback.selectFragment("female", mContact);

                        }
                    }
                });

        Button femaleBtn = findViewById(R.id.female_button);
        femaleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        String key = writeClientContact();
                        if (!key.equals("")) {
                            Intent intent = new Intent(AddClientContactActivity.this, AddClientFMeasure.class);
                            intent.putExtra("key", key);
                            AddClientContactActivity.this.startActivity(intent);
                            // mCallback.selectFragment("female", mContact);

                        }
                    }
                });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeClientContact();
            }
        });
        fab.setVisibility(View.VISIBLE);

    }

    public String writeClientContact() {
        String key = "";
        ClientContactDTO contact = readContact();
        //result = ValidateInput(contact.getFirstName(), contact.getLastName());
        NestedScrollView ContactInfo = this.findViewById(R.id.contact_info_frame);

        key = mClientContactInfo.child( mUser.getUid()).child("contacts").push().getKey();
        mClientContactInfo.child( mUser.getUid()).child("contacts").child(key).setValue(contact);
        if (!key.equals("")) {
            Snackbar.make(ContactInfo, "Saving Contact", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(ContactInfo, "Unable to save Please input first and last Name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return key;
    }

    private boolean ValidateInput(String fName, String lName) {
        boolean result = false;
        if (fName.equals("") || lName.equals("")) {
            //Snackbar.make(AddClientContactActivity,this, "Please enter first and last Name to Save.", Snackbar.LENGTH_LONG)
            //         .setAction("Action", null).show();
        } else result = true;

        return result;
    }

    private ClientContactDTO readContact() {

        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText middleName = findViewById(R.id.middle_name);
        EditText emailAddress = findViewById(R.id.email_address);
        EditText phoneNumber = findViewById(R.id.phone_number);
        EditText address = findViewById(R.id.address);

        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String mName = middleName.getText().toString();
        String email = emailAddress.getText().toString();
        String phone = phoneNumber.getText().toString();
        String addr = address.getText().toString();

        ClientContactDTO mContact = new ClientContactDTO();

        mContact.setFirstName(fName);
        mContact.setLastName(lName);
        mContact.setMiddleName(mName);
        mContact.setEmailAddress(email);
        mContact.setPhoneNumber(phone);
        mContact.setAddress(addr);

        return mContact;
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {

    }

    public interface callOtherFragment {
        void selectFragment(String otherFragment, ClientContactDTO contactInfo);
    }
}