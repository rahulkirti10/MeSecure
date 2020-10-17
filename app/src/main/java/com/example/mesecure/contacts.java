package com.example.mesecure;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.Map;

public class contacts extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    StorageReference reff;
    EditText search;
    ImageButton searchButton,back;
    String uidReceiver,ReceiverName;
    Button add;
    TextView view;
    String uid;
    ProgressBar progressBar,barImage;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Details/Users/Number");
        searchButton = findViewById(R.id.searchButton);
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
        img= findViewById(R.id.viewImage);
        img.setVisibility(View.INVISIBLE);
        progressBar=findViewById(R.id.progressbar);
        barImage=findViewById(R.id.barImage);
        barImage.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        view=findViewById(R.id.viewname);
        view.setVisibility(View.INVISIBLE);
        view = findViewById(R.id.aftersearch);
        view.setVisibility(View.INVISIBLE);
        back=findViewById(R.id.back);
        back.setVisibility(View.INVISIBLE);
        add=findViewById(R.id.add);
        add.setVisibility(View.INVISIBLE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReceiver();
                String mobile =search.getText().toString();
                Intent intent= new Intent(contacts.this,chatRoom.class);
                intent.putExtra("UserId",uidReceiver);
                startActivity(intent);

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchButton.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                view=findViewById(R.id.textView10);
                view.setVisibility(View.VISIBLE);
                view=findViewById(R.id.partner);
                view.setVisibility(View.VISIBLE);
                view=findViewById(R.id.aftersearch);
                view.setVisibility(View.INVISIBLE);
                barImage.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
                view= findViewById(R.id.viewname);
                view.setVisibility(View.INVISIBLE);
                add.setVisibility(View.INVISIBLE);
                img.setVisibility(View.INVISIBLE);
                search.setText("");
                reff=null;
                databaseReference=firebaseDatabase.getReference("Details/Users/Number");
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                search = findViewById(R.id.search);
                if(search.getText().toString().isEmpty()||search.getText().toString().length()!=10)
                {
                    search.setError("Enter a valid Number");

                }
                else
                {
                    finduid();
                }
            }
        });
    }
    private void finduid()

    {
        progressBar.setVisibility(View.VISIBLE);
        view=findViewById(R.id.textView10);
        view.setVisibility(View.INVISIBLE);
        view=findViewById(R.id.partner);
        view.setVisibility(View.INVISIBLE);
        search.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        final String str= search.getText().toString();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    uidReceiver = snapshot.child(str).getValue().toString();
                    FindName(uidReceiver);
                } catch (Exception e) {
                    searchButton.setVisibility(View.VISIBLE);
                    search.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    view=findViewById(R.id.textView10);
                    view.setVisibility(View.VISIBLE);
                    view=findViewById(R.id.partner);
                    view.setVisibility(View.VISIBLE);
                    view=findViewById(R.id.aftersearch);
                    view.setVisibility(View.INVISIBLE);
                    back.setVisibility(View.INVISIBLE);
                    view= findViewById(R.id.viewname);
                    view.setVisibility(View.INVISIBLE);
                    add.setVisibility(View.INVISIBLE);
                    Toast.makeText(contacts.this,"Number not found",Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void addReceiver()
    {
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Details/Users/SenderReceiver/"+uid).child(uidReceiver);
        databaseReference.setValue(uidReceiver);
    }
    private void FindName(final String uidReceiver)
    {
        databaseReference=firebaseDatabase.getReference("Details/Users/Name");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                view.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                view= findViewById(R.id.aftersearch);
                view.setVisibility(View.VISIBLE);
                ReceiverName= snapshot.child(uidReceiver).getValue().toString();
                view=findViewById(R.id.viewname);
                add.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                view.setText(ReceiverName);
                try {
                    findImage(uidReceiver);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void findImage(String uidReceiver) throws IOException {
        img.setVisibility(View.VISIBLE);
        barImage.setVisibility(View.VISIBLE);
        reff = FirebaseStorage.getInstance().getReferenceFromUrl("gs://me-993cd.appspot.com/"+uidReceiver).child(uidReceiver+".jpg");
        final File local= File.createTempFile(uidReceiver,"jpg");
        reff.getFile(local).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                Bitmap map = BitmapFactory.decodeFile(local.getAbsolutePath());
                barImage.setVisibility(View.INVISIBLE);
                img.setImageBitmap(map);
                map=null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(contacts.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

    }
}