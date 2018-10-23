/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/


package com.wanliss.kurt.hmByElla;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wanliss.kurt.hmByElla.DTO.StoreDisplayDTO;

import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.NumberViewHolder> {

    private static final String TAG = GalleryAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private final List<StoreDisplayDTO> displayList;
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private final View mView;
    private final int mNumberItems;

    public GalleryAdapter(List<StoreDisplayDTO> displayList, Context mainActivityContext) {
        mNumberItems = displayList.size();
        mOnClickListener = (ListItemClickListener) mainActivityContext; //listener;
        this.displayList = displayList;
        mView = ((Activity) mainActivityContext).getWindow().getDecorView().findViewById(R.id.drawer_layout);
    }

    @NonNull
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new NumberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(displayList.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(StoreDisplayDTO clickedMovie);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {
        final Context thisContext;
        final ImageView posterImage;
        final TextView posterText;


        NumberViewHolder(View itemView) {
            super(itemView);
            thisContext = itemView.getContext();
            posterImage = itemView.findViewById(R.id.posterImage);
            posterText = itemView.findViewById(R.id.psTextFrame);
            itemView.setOnClickListener(this);
        }


        void bind(StoreDisplayDTO movie) {
            StorageReference dateRef = mStorage.getReference().child(movie.getThumbnail());
            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Picasso.with(thisContext).load(downloadUrl).into(posterImage);
                }
            });
            posterText.setText(movie.getName());
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            StoreDisplayDTO movieInformation = displayList.get(clickedPosition);
            mOnClickListener.onListItemClick(movieInformation);

            Snackbar.make(mView.findViewById(R.id.drawer_layout), movieInformation.getName(),
                    Snackbar.LENGTH_SHORT)
                    .show();
        }

    }
}
