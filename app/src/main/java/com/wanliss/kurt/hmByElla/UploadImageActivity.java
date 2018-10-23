/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.wanliss.kurt.hmByElla.DTO.StoreDisplayDTO;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class UploadImageActivity extends AppCompatActivity implements GlobalLogin.LoginListener {
    private static final int PICK_IMAGE_REQUEST = 234;
    private final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    private EditText mPicDate;
    private ImageView mImageView;
    private Uri mFilePath;
    private StorageReference mStorageRef;
    private Calendar mDatePicker;
    private TextView mOrderLabel;
    private EditText mImageTitle;
    private EditText mOrderText;
    private TextView mNameLabel;
    private EditText mName;
    private TextView mNotesLabel;
    private EditText mNotes;
    private Switch mGroup;
    private StoreDisplayDTO clickedImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_activity);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);

        clickedImage = (StoreDisplayDTO) getIntent().getSerializableExtra("clickedImage");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mFilePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mFilePath);
                mImageView.setImageBitmap(bitmap);
                mImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (mFilePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            SimpleDateFormat timeF = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String time = timeF.format(Calendar.getInstance().getTime());
            String group = mGroup.isChecked() ? "true" : "false";
            final StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata("title", mImageTitle.getText().toString())
                    .setCustomMetadata("date", mPicDate.getText().toString())
                    .setCustomMetadata("time", time)
                    .setCustomMetadata("group", group)
                    .setCustomMetadata("groupOrder", mOrderText.getText().toString())
                    .setCustomMetadata("groupName", mName.getText().toString())
                    .setCustomMetadata("notes", mNotes.getText().toString())
                    .build();

            assert mUser != null;
            String extension = "";

            int i = Objects.requireNonNull(mFilePath.getPath()).lastIndexOf('.');
            if (i > 0) {
                extension = mFilePath.getPath().substring(i + 1);
            }
            String fileLocation;

            if (group.equals("true") && !mOrderText.getText().toString().equals("1")) {
                fileLocation = "GroupImages/" + mName.getText().toString() + "/" + mImageTitle.getText().toString();
            } else if (group.equals("true") && mOrderText.getText().toString().equals("1")) {
                fileLocation = "Gallery/" + mName.getText().toString();
            } else
                fileLocation = "Gallery/" + mImageTitle.getText().toString();


            final StorageReference riversRef = mStorageRef.child(fileLocation + extension);

            riversRef.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.updateMetadata(metadata);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "No file chosen! ", Toast.LENGTH_LONG).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mPicDate.setText(sdf.format(mDatePicker.getTime()));
    }

    private void showDatePicker() {
        mDatePicker = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mDatePicker.set(Calendar.YEAR, year);
                mDatePicker.set(Calendar.MONTH, monthOfYear);
                mDatePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        new DatePickerDialog(UploadImageActivity.this, date, mDatePicker
                .get(Calendar.YEAR), mDatePicker.get(Calendar.MONTH),
                mDatePicker.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void toggleGroupSwitch(boolean isChecked) {
        if (isChecked) {
            mOrderLabel.setVisibility(View.VISIBLE);
            mOrderText.setVisibility(View.VISIBLE);
            mNameLabel.setVisibility(View.VISIBLE);
            mName.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.group_name);
            mNotesLabel.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.notes_label);
            mNotes.setLayoutParams(params2);
        } else {
            mOrderLabel.setVisibility(View.GONE);
            mOrderText.setVisibility(View.GONE);
            mNameLabel.setVisibility(View.GONE);
            mName.setVisibility(View.GONE);

            mOrderText.setText("");
            mName.setText("");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.group_switch);
            mNotesLabel.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.notes_label);
            mNotes.setLayoutParams(params2);
        }
    }

    private void writeContact(StoreDisplayDTO clickedImage) {
        mImageTitle.setText(clickedImage.getTitle());
        mOrderText.setText(clickedImage.getGroupOrder());
        mName.setText(clickedImage.getName());
        mNotes.setText(clickedImage.getNotes());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference dateRef = storage.getReference().child(clickedImage.getPath());
        dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Picasso.with(UploadImageActivity.this).load(downloadUrl).into(mImageView);
            }
        });
    }

    @Override
    public void onCheckedLogIn(GlobalLogin.dataSet admin) {
        if (admin == GlobalLogin.dataSet.SET_TRUE) {

            Button buttonChoose;
            Button buttonUpload;

            SimpleDateFormat dateF = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
            String todayDate = dateF.format(Calendar.getInstance().getTime());

            mStorageRef = FirebaseStorage.getInstance().getReference();

            buttonChoose = this.findViewById(R.id.buttonChoose);
            buttonUpload = this.findViewById(R.id.buttonUpload);
            mGroup = findViewById(R.id.group_switch);
            mImageView = this.findViewById(R.id.imageView);
            mPicDate = this.findViewById(R.id.pic_date);
            mPicDate.setText(todayDate);
            mOrderLabel = this.findViewById(R.id.group_order_label);
            mImageTitle = this.findViewById(R.id.imageTitle);
            mOrderText = this.findViewById(R.id.order_text);
            mNameLabel = this.findViewById(R.id.group_name_label);
            mName = this.findViewById(R.id.group_name);
            mNotesLabel = this.findViewById(R.id.notes_label);
            mNotes = this.findViewById(R.id.notes);

            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });

            buttonUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadFile();
                }
            });

            mGroup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleGroupSwitch(isChecked);
                }
            });

            mPicDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();
                }
            });

            if (clickedImage != null) {
                writeContact(clickedImage);
                if (clickedImage.isGroup().equals("true")) {
                    mGroup.setChecked(true);
                    toggleGroupSwitch(true);
                }
            }
        } else {
            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }
}