package com.wanliss.kurt.sewingmate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wanliss.kurt.sewingmate.DTO.ClientContactDTO;

import java.util.List;

public class ContactRecyclerViewAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    protected Context context;
    private List<ClientContactDTO> clients;

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
