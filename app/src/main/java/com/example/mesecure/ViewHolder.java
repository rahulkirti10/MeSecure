package com.example.mesecure;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
public class ViewHolder extends RecyclerView.ViewHolder
{
    TextView Name;
    TextView Mobile;
    TextView userId,border;
    ViewHolder(View itemView, final ListAdapter.OnItemClickListener listener)
    {
        super(itemView);
        Name=itemView.findViewById(R.id.Name);
        Mobile=itemView.findViewById(R.id.mobile);
        userId=itemView.findViewById(R.id.ReceiverUid);
        border=itemView.findViewById(R.id.border2);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }
}
