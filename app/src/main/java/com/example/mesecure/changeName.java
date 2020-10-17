package com.example.mesecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class changeName extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    String name,provider;
    Button cont;
    EditText uname;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        provider= FirebaseAuth.getInstance().getUid();
        cont = findViewById(R.id.cont);
        bar = findViewById(R.id.bar);
        bar.setVisibility(View.INVISIBLE);
        uname = findViewById(R.id.userName);
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setVisibility(View.VISIBLE);
                uname.setVisibility(View.INVISIBLE);
                cont.setVisibility(View.INVISIBLE);
                updateName();
                Intent intent=new Intent(changeName.this,chatPage.class);
                startActivity(intent);
            }
        });

    }
    private void updateName()
    {
        name=uname.getText().toString();
        firebaseDatabase= FirebaseDatabase.getInstance();
        String uid=FirebaseAuth.getInstance().getUid();
        reference = firebaseDatabase.getReference("Details/Users/Name").child(provider);
        reference.setValue(name);
        reference = firebaseDatabase.getReference("Details/Users/AllUsers").child(uid);
        String mobile=getIntent().getStringExtra("mobile");
        HashMap<String,String> map=new HashMap<String,String>();
        map.put("Name",name);
        map.put("UserId",uid);
        map.put("Mobile",mobile);
        reference.setValue(map);
        Toast.makeText(changeName.this,"Updated Successfully",Toast.LENGTH_LONG).show();

    }
}