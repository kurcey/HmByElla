/*
        Copyright 2018 Kurt Wanliss

        All rights reserved under the copyright laws of the United States
        and applicable international laws, treaties, and conventions.

        You may freely redistribute and use this sample code, with or
        without modification, provided you include the original copyright
        notice and use restrictions.

*/

package com.wanliss.kurt.hmByElla;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wanliss.kurt.hmByElla.DTO.ClientContactDTO;

import java.util.List;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    private ClientContactDTO mContact;
    public final List<ClientContactDTO> mClientObject;
    public final TextView firstName;
    public final TextView lastName;

    public ContactViewHolder(final View itemView, final List<ClientContactDTO> clientObject) {
        super(itemView);
        this.mClientObject = clientObject;
        firstName = itemView.findViewById(R.id.first_name);
        lastName = itemView.findViewById(R.id.last_name);

    }

    public void setContact(ClientContactDTO contact) {
        this.mContact = contact;

    }
}
