package com.wanliss.kurt.hmByElla;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wanliss.kurt.hmByElla.DTO.StoreDisplayDTO;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetConfigureActivity WidgetConfigureActivity}
 */
public class Widget extends AppWidgetProvider {
    private static final String TAG = Widget.class.getSimpleName();

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {
        DatabaseReference mGroupImagesDbRef = null;
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mGroupImagesDbRef = mDatabase.getReference(context.getString(R.string.thumbnail_galery_db_location));

        final FirebaseStorage mStorage = FirebaseStorage.getInstance();

        CharSequence widgetText = WidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
/*
        if(variable==null){
            variable = getFromSharedPrefs();
        }
*/


        Query queryRef = mGroupImagesDbRef.orderByChild(context.getString(R.string.widget_db_orderby)).limitToFirst(1);
        queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoreDisplayDTO store = dataSnapshot.getValue(StoreDisplayDTO.class);

                    StorageReference dateRef = mStorage.getReference().child(store.getThumbnail());
                    dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            // Picasso.with(context).load(downloadUrl).into(posterImage);
                            Picasso.with(context)
                                    .load(downloadUrl)
                                    .into(views, R.id.imageView, new int[]{appWidgetId});
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                            //  views.setImageViewBitmap(R.id.imageView , );
                        }
                    });
                    // posterText.setText(movie.getName());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, context.getString(R.string.widget_error_msg), databaseError.toException());
            }
        });

        // Instruct the widget manager to update the widget
        // appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}