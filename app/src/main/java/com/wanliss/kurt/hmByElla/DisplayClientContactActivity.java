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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

public class DisplayClientContactActivity extends AppCompatActivity implements GlobalLogin.LoginListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_client_contact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);

        final ClientContactDTO clickedClient = (ClientContactDTO) getIntent().getSerializableExtra("clickedClient");
        writeContact(clickedClient);

        Button maleBtn = findViewById(R.id.male_button);
        maleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(DisplayClientContactActivity.this, DisplayClientMMeasureActivity.class);
                        intent.putExtra("key", clickedClient.getId());
                        DisplayClientContactActivity.this.startActivity(intent);
                    }
                });

        Button femaleBtn = findViewById(R.id.female_button);
        femaleBtn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(DisplayClientContactActivity.this, DisplayClientFMeasureActivity.class);
                        intent.putExtra("key", clickedClient.getId());
                        DisplayClientContactActivity.this.startActivity(intent);
                    }
                });
    }


    private void writeContact(ClientContactDTO clickedClient) {

        TextView firstName = findViewById(R.id.first_name);
        TextView lastName = findViewById(R.id.last_name);
        TextView middleName = findViewById(R.id.middle_name);
        TextView emailAddress = findViewById(R.id.email_address);
        TextView phoneNumber = findViewById(R.id.phone_number);
        TextView address = findViewById(R.id.address);

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