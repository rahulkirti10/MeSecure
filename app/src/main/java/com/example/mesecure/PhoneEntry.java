package com.example.mesecure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class PhoneEntry extends AppCompatActivity {
    String phoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_entry);
        findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ph=(EditText)findViewById(R.id.editTextPhone);
                phoneNo=ph.getText().toString();
                if(phoneNo.isEmpty())
                {
                    ph.setError("Number can't be Empty");
                }
                else if(phoneNo.length()!=10)
                    ph.setError("Enter a Valid Number");
                else {
                    Intent intent = new Intent(PhoneEntry.this, nextActivity.class);
                    intent.putExtra("phoneNo", phoneNo);

                    startActivity(intent);
                }
            }
        });
    }
}