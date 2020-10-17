package com.example.mesecure;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;
public class ListAdapter extends RecyclerView.Adapter<ViewHolder>   {

    List<ListUser> list = Collections.emptyList();
    private OnItemClickListener mlistener;
    Context context;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mlistener=listener;
    }
    public ListAdapter(List<ListUser> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.list_item,
                parent, false);
        ViewHolder viewHolder = new ViewHolder(photoView, mlistener);
        return viewHolder;
    }

    @Override
    public void
    onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        viewHolder.Name
                .setText(list.get(position).Name);
        viewHolder.Mobile
                .setText(list.get(position).Mobile);
        viewHolder.userId
                .setText(list.get(position).UserId);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }



}







