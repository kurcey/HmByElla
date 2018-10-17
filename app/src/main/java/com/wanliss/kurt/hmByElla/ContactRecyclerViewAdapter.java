/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.List;

class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    protected final Context context;
    private final List<ClientContactDTO> clients;

    public ContactRecyclerViewAdapter(Context context, List<ClientContactDTO> clients) {
        this.clients = clients;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactViewHolder viewHolder = null;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_list, parent, false);
        viewHolder = new ContactViewHolder(layoutView, clients);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(clients.get(position));
        holder.firstName.setText(clients.get(position).getFirstName());
        holder.lastName.setText(clients.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return this.clients.size();
    }
}
