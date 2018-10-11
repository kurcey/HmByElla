/* Copyright 2018 Kurt Wanliss
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 *
 */

package com.wanliss.kurt.sewingmate;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wanliss.kurt.sewingmate.DTO.StoreDisplayDTO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.NumberViewHolder> {

    private static final String TAG = PosterAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private final List<StoreDisplayDTO> displayList;
    private final String mImageSize;
    private int mNumberItems;

    public PosterAdapter(List<StoreDisplayDTO> displayList, Context mainActivityContext) {
        mNumberItems = displayList.size();
        mOnClickListener = (ListItemClickListener) mainActivityContext; //listener;
        this.displayList = displayList;
        mImageSize = mainActivityContext.getString(R.string.imageSize);
    }


    /**
     * @param additionaldisplayList
     */
    public void updatedisplayList(ArrayList<StoreDisplayDTO> additionaldisplayList) {
        this.displayList.addAll(additionaldisplayList);
        this.mNumberItems = displayList.size();
        Log.d(TAG, "adding additional Recycler views " + getItemCount());
    }


    /*

    @NonNull
    @Override
    */
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
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

        final ImageView posterImage;
        final TextView posterText;
        final Context thisContext;

        /**
         * @param itemView
         */
        public NumberViewHolder(View itemView) {
            super(itemView);
            thisContext = itemView.getContext();
            posterImage = itemView.findViewById(R.id.posterImage);
            posterText = itemView.findViewById(R.id.psTextFrame);
            itemView.setOnClickListener(this);
        }

        /**
         * @param movie
         */
        void bind(StoreDisplayDTO movie) {
            URL imagePath = getUrlImage(movie.getThumbnail());
            posterText.setText(movie.getName());
            Picasso.with(thisContext).load(imagePath.toString()).into(posterImage);
        }

        /**
         * @param v
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            StoreDisplayDTO movieInformation = displayList.get(clickedPosition);
            mOnClickListener.onListItemClick(movieInformation);
        }

        private URL getUrlImage(String imageParam) {
            Uri builtUri = Uri.parse(imageParam);

            URL url = null;
            try {
                url = new URL(builtUri.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return url;
        }
    }
}
