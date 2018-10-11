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

package com.example.android.popularMovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import static com.example.android.popularMovies.NetworkUtils.getUrlImage;


public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.NumberViewHolder> {

    private static final String TAG = PosterAdapter.class.getSimpleName();
    private static int viewHolderCount;
    final private ListItemClickListener mOnClickListener;
    private final ArrayList<MovieDBO> movieList;
    private final String mImageUrl;
    private final String mImageSize;
    private int mNumberItems;

    public PosterAdapter(int numberOfItems, ListItemClickListener listener, ArrayList<MovieDBO> movieList, Context mainActivityContext) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        this.movieList = movieList;
        viewHolderCount = 0;
        mImageUrl = mainActivityContext.getString(R.string.imageUrl);
        mImageSize = mainActivityContext.getString(R.string.imageSize);
    }


    /**
     * @param additionalMovieList
     */
    public void updateMovieList(ArrayList<MovieDBO> additionalMovieList) {
        this.movieList.addAll(additionalMovieList);
        this.mNumberItems = movieList.size();
        Log.d(TAG, "adding additional Recycler views " + getItemCount());
    }

    /**
     * @param viewGroup
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.poster_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        viewHolderCount++;
        return viewHolder;
    }

    /**
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(movieList.get(position));
    }

    /**
     * @return
     */
    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(MovieDBO clickedMovie);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder
            implements OnClickListener {

        final ImageView posterImage;
        final Context thisContext;

        /**
         * @param itemView
         */
        public NumberViewHolder(View itemView) {
            super(itemView);
            thisContext = itemView.getContext();
            posterImage = itemView.findViewById(R.id.posterImage);

            itemView.setOnClickListener(this);
        }

        /**
         * @param movie
         */
        void bind(MovieDBO movie) {
            URL imagePath = getUrlImage(mImageUrl, mImageSize, movie.getPosterPath());
            Picasso.with(thisContext).load(imagePath.toString()).into(posterImage);
        }

        /**
         * @param v
         */
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            MovieDBO movieInformation = movieList.get(clickedPosition);
            mOnClickListener.onListItemClick(movieInformation);
        }
    }
}
