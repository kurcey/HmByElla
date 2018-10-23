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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.Objects;

public class AddClientContactActivity extends AppCompatActivity implements GlobalLogin.LoginListener {

    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mClientContactInfo = null;
    private String mPassKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_client_contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);
        mClientContactInfo = mDatabase.getReference(getString(R.string.main_user_db_location));

        ClientContactDTO clickedClient = (ClientContactDTO) getIntent().getSerializableExtra("clickedClient");
        if (clickedClient != null) {
            mPassKey = clickedClient.getId();
            writeContact(clickedClient);
        }
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

    private String writeClientContact() {
        String key;
        if (mPassKey.equals(""))
            key = mClientContactInfo.child(getString(R.string.main_user_db_contact_location)).push().getKey();
        else
            key = mPassKey;
        ClientContactDTO contact = readContact(key);
        //result = ValidateInput(contact.getFirstName(), contact.getLastName());
        NestedScrollView ContactInfo = this.findViewById(R.id.contact_info_frame);


        mClientContactInfo.child(getString(R.string.main_user_db_contact_location)).child(Objects.requireNonNull(key)).setValue(contact);
        if (!key.equals("")) {
            Snackbar.make(ContactInfo, "Saving Contact", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else {
            Snackbar.make(ContactInfo, "Unable to save Please input first and last Name", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        return key;
    }

    private ClientContactDTO readContact(String id) {

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
        mContact.setId(id);

        return mContact;
    }


    private void writeContact(ClientContactDTO clickedClient) {

        EditText firstName = findViewById(R.id.first_name);
        EditText lastName = findViewById(R.id.last_name);
        EditText middleName = findViewById(R.id.middle_name);
        EditText emailAddress = findViewById(R.id.email_address);
        EditText phoneNumber = findViewById(R.id.phone_number);
        EditText address = findViewById(R.id.address);

        firstName.setText(clickedClient.getFirstName());
        lastName.setText(clickedClient.getLastName());
        middleName.setText(clickedClient.getMiddleName());
        emailAddress.setText(clickedClient.getEmailAddress());
        phoneNumber.setText(clickedClient.getPhoneNumber());
        address.setText(clickedClient.getAddress());
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {

    }
}