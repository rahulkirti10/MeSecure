package com.example.mesecure;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;
public class nextActivity extends AppCompatActivity {
    private FirebaseAuth firebase;
    private EditText otp;
    private Button verify;
    private TextView resend;
    private  String firecode;
    String mobile;
    TextView text;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        firebase=FirebaseAuth.getInstance();
        otp = (EditText)findViewById(R.id.userCode);
        Intent intent=getIntent();
        mobile= intent.getStringExtra("phoneNo");
        TextView dis=(TextView) findViewById(R.id.display);
        dis.setText("+91"+mobile);
        SendVerification(mobile);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        verify = (Button)findViewById(R.id.verify);
        resend = (TextView)findViewById(R.id.resend);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code=otp.getText().toString().trim();
                if (code.isEmpty() || code.length()!= 6)
                {
                    otp.setError("Enter valid code");
                    otp.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp.setText("");
                SendVerification(mobile);
                Toast.makeText(nextActivity.this,"Code Resend",Toast.LENGTH_LONG).show();
            }
        });
    }
    private void verifyCode(String code)
    {
        progressBar.setVisibility(View.VISIBLE);
        otp.setVisibility(View.INVISIBLE);
        verify.setVisibility(View.INVISIBLE);
        resend.setVisibility(View.INVISIBLE);
        text = findViewById(R.id.textView9);
        text.setVisibility(View.INVISIBLE);
        text=findViewById(R.id.display);
        text.setVisibility(View.INVISIBLE);
        text=findViewById(R.id.textView8);
        text.setVisibility(View.INVISIBLE);
        text=findViewById(R.id.textView2);
        text.setVisibility(View.INVISIBLE);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firecode,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebase.signInWithCredential(credential)
                .addOnCompleteListener(nextActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(nextActivity.this,"Login Successfully",Toast.LENGTH_LONG).show();
                            UpdateDatabase();
                            Intent intent = new Intent(nextActivity.this, mainPageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("mobile",mobile);
                            startActivity(intent);

                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(nextActivity.this,"Login Failed",Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void SendVerification(String mobile)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91"+mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                otp.setText(code);
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(nextActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            firecode=s;
        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent = new Intent(nextActivity.this,mainPageActivity.class);
            startActivity(intent);
        }
    }
    private void UpdateDatabase()
    {
        String uid=firebase.getUid();
        firebaseDatabase=FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Details/Users/Number").child(mobile);
        reference.setValue(uid);
        reference =firebaseDatabase.getReference("Details/Users/UserId").child(uid);
        reference.setValue(mobile);
    }
}