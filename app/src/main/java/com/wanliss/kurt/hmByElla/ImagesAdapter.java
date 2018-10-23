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

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.NumberViewHolder> {

    private static final String TAG = ImagesAdapter.class.getSimpleName();
    private final List<StoreDisplayDTO> displayList;
    private final FirebaseStorage mStorage = FirebaseStorage.getInstance();
    private final View mView;
    private final int mNumberItems;

    public ImagesAdapter(List<StoreDisplayDTO> displayList, Context galleryActivityContext) {
        mNumberItems = displayList.size();
        this.displayList = displayList;
        mView = ((Activity) galleryActivityContext).getWindow().getDecorView().findViewById(R.id.drawer_layout);
    }

    @NonNull
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.gallery_row;
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
    }

    class NumberViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        final ImageView galleryImage;
        final TextView galleryText;
        final Context thisContext;

        NumberViewHolder(View itemView) {
            super(itemView);
            thisContext = itemView.getContext();
            galleryImage = itemView.findViewById(R.id.galleryImage);
            galleryText = itemView.findViewById(R.id.psTextFrame);
            itemView.setOnClickListener(this);
        }

        void bind(StoreDisplayDTO movie) {
            galleryText.setText(movie.getName());

            StorageReference dateRef = mStorage.getReference().child(movie.getThumbnail());
            dateRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Picasso.with(thisContext).load(downloadUrl).into(galleryImage);
                }
            });
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            StoreDisplayDTO movieInformation = displayList.get(clickedPosition);
            //mOnClickListener.onListItemClick(movieInformation);

            Snackbar.make(mView.findViewById(R.id.drawer_layout), movieInformation.getName(),
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }
}