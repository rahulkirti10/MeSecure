package com.example.mesecure;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Collections;
import java.util.List;
public class ChatAdapter extends RecyclerView.Adapter<chatHolder> {

    List<Chat> list = Collections.emptyList();
    Context context;
    FirebaseAuth firebaseAuth;
    public static final int right_chat = 1, left_chat = 0;

    public ChatAdapter(List<Chat> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public chatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==left_chat) {
            View photoView = inflater.inflate(R.layout.chat_item_left, parent, false);
            chatHolder Holder = new chatHolder(photoView);
            return Holder;
        }
        else {
            View photoView = inflater.inflate(R.layout.chat_item_right, parent, false);
            chatHolder Holder = new chatHolder(photoView);
            return Holder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull chatHolder Holder, final int position) {
        try {
            DES decryption= new DES("8010447895");
            Holder.sender.setText(decryption.Decrypt(list.get(position).message));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(
            RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemViewType(int position) {
        firebaseAuth = FirebaseAuth.getInstance();
        String uid = firebaseAuth.getUid().toString();
        if (list.get(position).getSenderUid().equals(uid)) {
            return right_chat;
        }
        else
        {
            return left_chat;
        }
    }
}







