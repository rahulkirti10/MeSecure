package com.example.mesecure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class chatRoom extends AppCompatActivity {
    ImageButton back,send;
    TextView viewName;
    ImageView img;
    EditText message;
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String UserId;
    private List<Chat> chatList;
    ChatAdapter chatAdapter;
    DES encrytion=new DES("8010447895");

    public chatRoom() throws Exception {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        back = findViewById(R.id.backbutton);
        viewName=findViewById(R.id.viewName);
        img = findViewById(R.id.viewImage);
        send= findViewById(R.id.sendButton);
        message=findViewById(R.id.sendText);
        recyclerView = findViewById(R.id.chatView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        UserId=getIntent().getStringExtra("UserId");
        setNameById();
        readMessage(UserId);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mess=message.getText().toString();
                mess=encrytion.encrypt(mess);
                message.setText("");
                sendMessage(mess);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatRoom.this,chatPage.class);
                startActivity(intent);
            }
        });
    }
    private void setNameById()
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Details/Users/AllUsers/"+UserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Name").getValue().toString();
                viewName.setText(name);
                try {
                    setImage();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setImage() throws IOException {
        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://me-993cd.appspot.com/"+UserId).child(UserId+".jpg");
        final File local= File.createTempFile(UserId,"jpg");
        storageReference.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap map = BitmapFactory.decodeFile(local.getAbsolutePath());
                img.setImageBitmap(map);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(chatRoom.this,"Error",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendMessage(String message)
    {
        String uid=FirebaseAuth.getInstance().getUid();
        DatabaseReference reff=FirebaseDatabase.getInstance().getReference();
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("message",message);
        map.put("senderUid",uid);
        map.put("receiverUid",UserId);
        reff.child("Chats").push().setValue(map);
    }
    private void readMessage( final String receiverUid)
    {
        chatList=new ArrayList<>();
        final String uid=FirebaseAuth.getInstance().getUid();
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Chats");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    Chat chat=ds.getValue(Chat.class);
                    if(chat.getReceiverUid().equals(uid)&&chat.getSenderUid().equals(receiverUid)||chat.getReceiverUid().equals(receiverUid)&&chat.getSenderUid().equals(uid)) {

                        chatList.add(chat);
                    }
                    chatAdapter= new ChatAdapter(chatList,chatRoom.this);
                    recyclerView.setAdapter(chatAdapter);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
  }
        });
    }
}