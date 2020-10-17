package com.example.mesecure;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
public class mainPageActivity extends AppCompatActivity {
    private ImageView choose;
    private StorageReference str;
    String provider;
    private Uri image;
    Button logout;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        logout=(Button)findViewById(R.id.logout);
        choose=(ImageView)findViewById(R.id.cng_photo);
        provider= FirebaseAuth.getInstance().getUid();
        progressBar = (ProgressBar)findViewById(R.id.progress);
        str= FirebaseStorage.getInstance().getReference(provider);
        progressBar.setVisibility(View.INVISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                choose.setVisibility(View.INVISIBLE);
                logout.setVisibility(View.INVISIBLE);
                ImageView imageView=(ImageView)findViewById(R.id.userimage);
                imageView.setVisibility(View.INVISIBLE);
                FileUpload();
            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==100)
        {
            image=data.getData();
            ImageView imageView=(ImageView)findViewById(R.id.userimage);
            imageView.setImageURI(image);
        }
    }
    private String imageExtension(Uri uri)
    {
        ContentResolver cr=getContentResolver();
        MimeTypeMap mp= MimeTypeMap.getSingleton();
        return mp.getExtensionFromMimeType(cr.getType(uri));
    }
    private void FileUpload()
    {
        StorageReference ref= str.child(provider+"."+imageExtension(image));


        ref.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(mainPageActivity.this,"Upload Successfully",Toast.LENGTH_LONG).show();
                        Intent intent=new Intent(mainPageActivity.this,changeName.class);
                        String mobile=getIntent().getStringExtra("mobile");
                        intent.putExtra("mobile",mobile);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(mainPageActivity.this,"Uplaod Failed",Toast.LENGTH_LONG).show();
                    }
                });
    }

}



