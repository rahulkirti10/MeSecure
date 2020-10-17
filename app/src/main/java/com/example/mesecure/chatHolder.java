package com.example.mesecure;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
public class chatHolder extends RecyclerView.ViewHolder
{
    TextView sender;
    chatHolder(View itemView)
    {
        super(itemView);
        sender=itemView.findViewById(R.id.show_chat);
    }
}
