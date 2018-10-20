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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.ArrayList;
import java.util.List;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.NumberViewHolder> {
    private static final String TAG = GalleryAdapter.class.getSimpleName();
    public final List<ClientContactDTO> clients;
    protected final Context context;
    final private ContactAdapter.ListItemClickListener mOnClickListener;
    private final View mView;
    private int mNumberItems;

    public ContactAdapter(Context context, List<ClientContactDTO> clients) {
        this.clients = clients;
        this.context = context;
        mNumberItems = clients.size();
        mOnClickListener = (ListItemClickListener) context; //listener;
        mView = ((Activity) context).getWindow().getDecorView().findViewById(R.id.drawer_layout);
    }

    public void updatedisplayList(ArrayList<ClientContactDTO> additionaldisplayList) {
        this.clients.addAll(additionaldisplayList);
        this.mNumberItems = clients.size();
        Log.d(TAG, "adding additional Recycler views " + getItemCount());
    }

    public NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.contact_row;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NumberViewHolder holder, int position) {
        holder.bind(clients.get(position));
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public interface ListItemClickListener {
        void onListItemClick(ClientContactDTO clickedMovie);
    }

    class NumberViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        final Context thisContext;
        final TextView firstName;
        final TextView lastName;

        NumberViewHolder(View itemView) {
            super(itemView);
            thisContext = itemView.getContext();
            firstName = itemView.findViewById(R.id.first_name);
            lastName = itemView.findViewById(R.id.last_name);
            itemView.setOnClickListener(this);
        }


        void bind(ClientContactDTO movie) {
            // System.out.println(movie.getFirstName());
            firstName.setText(movie.getFirstName());
            lastName.setText(movie.getLastName());
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            ClientContactDTO movieInformation = clients.get(clickedPosition);
            mOnClickListener.onListItemClick(movieInformation);

            Snackbar.make(mView.findViewById(R.id.drawer_layout), movieInformation.getFirstName(),
                    Snackbar.LENGTH_SHORT)
                    .show();

                  /*  Class destinationActivity = DetailsView.class;
        Intent startChildActivityIntent = new Intent(context, destinationActivity);
        startChildActivityIntent.putExtra("clickedMovie", clickedMovie);
        startActivity(startChildActivityIntent);
        */
        }

    }
}
