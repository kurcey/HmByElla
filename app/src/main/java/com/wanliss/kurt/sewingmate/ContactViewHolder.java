package com.wanliss.kurt.sewingmate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.wanliss.kurt.sewingmate.DTO.ClientContactDTO;

import java.util.List;

public class ContactViewHolder extends RecyclerView.ViewHolder {
    public ClientContactDTO mContact;
    public List<ClientContactDTO> mClientObject;
    public TextView firstName;
    public TextView lastName;

    public ContactViewHolder(final View itemView, final List<ClientContactDTO> clientObject) {
        super(itemView);
        this.mClientObject = clientObject;
        firstName = (TextView) itemView.findViewById(R.id.first_name);
        lastName = (TextView) itemView.findViewById(R.id.last_name);

    }

    public void setContact( ClientContactDTO contact){
        this.mContact = contact;

    }
}
