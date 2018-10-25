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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
    private static final String TAG = UploadImageActivity.class.getSimpleName();
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
  //  private EditText mName;
    private TextView mNotesLabel;
    private EditText mNotes;
    private Switch mGroup;
    private StoreDisplayDTO clickedImage = null;
    private ArrayAdapter<String> mImagesArrayAdapter;
    private Spinner mSpinner;
    private DatabaseReference mGroupImagesDbRef = null;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private String mSelectedGroup = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_image_activity);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        GlobalLogin.initialize_drawer(this);
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
            progressDialog.setTitle(getString(R.string.uploadimages_dialog_text));
            progressDialog.show();

            SimpleDateFormat timeF = new SimpleDateFormat(getString(R.string.uploadimages_time_formatt), Locale.getDefault());
            String time = timeF.format(Calendar.getInstance().getTime());
            String group = mGroup.isChecked() ? getString(R.string.uploadimages_true) : getString(R.string.uploadimages_false);
            if(!mOrderText.getText().toString().isEmpty())
                mSelectedGroup = mSpinner.getSelectedItem().toString();
            final StorageMetadata metadata = new StorageMetadata.Builder()
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_title), mImageTitle.getText().toString())
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_date), mPicDate.getText().toString())
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_time), time)
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_group), group)
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_groupOrder), mOrderText.getText().toString())
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_groupName), mSelectedGroup)
                    .setCustomMetadata(getString(R.string.uploadimages_metadata_notes), mNotes.getText().toString())
                    .build();

            assert mUser != null;
            String extension = "";

            int i = Objects.requireNonNull(mFilePath.getPath()).lastIndexOf('.');
            if (i > 0) {
                extension = mFilePath.getPath().substring(i + 1);
            }
            String fileLocation;

            if (group.equals(getString(R.string.uploadimages_true)) && !mOrderText.getText().toString().equals("1")) {
                fileLocation = getString(R.string.uploadimages_db_group_images) + mSpinner.getSelectedItem().toString() + getString(R.string.uploadimages_db_forward_slash) + mImageTitle.getText().toString();
            } else if (group.equals(getString(R.string.uploadimages_true)) && mOrderText.getText().toString().equals("1")) {
                fileLocation = getString(R.string.uploadimages_db_gallery) + mSpinner.getSelectedItem().toString();
            } else
                fileLocation = getString(R.string.uploadimages_db_gallery) + mImageTitle.getText().toString();


            final StorageReference riversRef = mStorageRef.child(fileLocation + extension);

            riversRef.putFile(mFilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            riversRef.updateMetadata(metadata);
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), getString(R.string.toast_file_uploaded), Toast.LENGTH_LONG).show();
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
                            progressDialog.setMessage(getString(R.string.toast_uploaded) + ((int) progress) + getString(R.string.toast_percent_sign_periods));
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_no_file_chosen), Toast.LENGTH_LONG).show();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType(getString(R.string.uploadimages_chooser_image_all));
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.uploadimages_intent_title)), PICK_IMAGE_REQUEST);
    }

    private void updateLabel() {
        String myFormat = getString(R.string.uploadimages_date_format);
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
            mSpinner.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.group_name);
            mNotesLabel.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.notes_label);
            mNotes.setLayoutParams(params2);

            mImagesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mImagesArrayAdapter);
        } else {
            mOrderLabel.setVisibility(View.GONE);
            mOrderText.setVisibility(View.GONE);
            mNameLabel.setVisibility(View.GONE);
            mSpinner.setVisibility(View.GONE);

            mOrderText.setText("");
            //mName.setText("");

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.BELOW, R.id.group_switch);
            mNotesLabel.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.addRule(RelativeLayout.BELOW, R.id.notes_label);
            mNotes.setLayoutParams(params2);
        }
    }

    private void writeContact(StoreDisplayDTO clickedImage) {
        mImagesArrayAdapter.add(clickedImage.getGroupName());
        mImageTitle.setText(clickedImage.getTitle());
        mOrderText.setText(clickedImage.getGroupOrder());
       // mName.setText(clickedImage.getName());
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

            SimpleDateFormat dateF = new SimpleDateFormat(getString(R.string.uploadimages_date_format), Locale.getDefault());
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
          //  mName = this.findViewById(R.id.group_name);
            mNotesLabel = this.findViewById(R.id.notes_label);
            mNotes = this.findViewById(R.id.notes);
            mSpinner = (Spinner) findViewById(R.id.group_name);

            clickedImage = (StoreDisplayDTO) getIntent().getSerializableExtra(getString(R.string.uploadimages_intent_key));

            mImagesArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, android.R.id.text1);
            mImagesArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mSpinner.setAdapter(mImagesArrayAdapter);
            mImagesArrayAdapter.notifyDataSetChanged();

            mGroupImagesDbRef = mDatabase.getReference(getString(R.string.thumbnail_galery_db_location));
            Query queryRef = mGroupImagesDbRef.orderByKey().limitToFirst(200);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StoreDisplayDTO image = dataSnapshot.getValue(StoreDisplayDTO.class);
                        mImagesArrayAdapter.add(image.getName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, getString(R.string.uploadimages_error_msg), databaseError.toException());
                }
            });

            buttonChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFileChooser();
                }
            });

            buttonUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String erroMsg = "";
                    if( mImageTitle.getText().toString().isEmpty() || mImageTitle.getText().toString().equals(""))
                        erroMsg +=  getString(R.string.uploadimages_error_msg_missing_title);
                    if( mGroup.isChecked() &&
                            (mSpinner.getSelectedItem().toString().isEmpty() || mSpinner.getSelectedItem().toString().equals(""))
                            )
                        erroMsg +=  getString(R.string.uploadimages_error_msg_missing_group);

                    if(erroMsg.equals(""))
                        uploadFile();
                    else {
                        Toast.makeText(getApplicationContext(), erroMsg , Toast.LENGTH_LONG).show();
                    }
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
                if (clickedImage.isGroup().equals(getString(R.string.uploadimages_true))) {
                    mGroup.setChecked(true);
                    toggleGroupSwitch(true);
                }
            }
            else mImagesArrayAdapter.add("");
        } else {

            Intent intent = new Intent(this, GalleryActivity.class);
            this.startActivity(intent);
        }
    }
}