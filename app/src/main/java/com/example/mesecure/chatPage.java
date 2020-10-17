package com.example.mesecure;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class chatPage extends AppCompatActivity {
    ImageButton mess;
    DatabaseReference databaseReference;
    Map<Integer,String> Allreceiver=new HashMap<>();
    ListAdapter adapter;
    RecyclerView recyclerView;
    List<ListUser> list = new ArrayList<>();
    String name,mobile,userid;
    int index=0;
    final String uid = FirebaseAuth.getInstance().getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);
        mess = findViewById(R.id.openmessage);
        databaseReference=FirebaseDatabase.getInstance().getReference("Details/Users/SenderReceiver/"+uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    String value = ds.getKey();
                    Allreceiver.put(index,value);
                    index++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        databaseReference=FirebaseDatabase.getInstance().getReference("Details/Users/AllUsers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()) {
                    if(Allreceiver.containsValue(ds.getKey())&&!uid.equals(ds.getKey())) {
                        name = snapshot.child(ds.getKey()).child("Name").getValue().toString();
                        mobile = snapshot.child(ds.getKey()).child("Mobile").getValue().toString();
                        userid = snapshot.child(ds.getKey()).child("UserId").getValue().toString();
                        list.add(new ListUser(name,mobile,userid));
                    }
                }
                adapter= new ListAdapter(list, getApplication());
                recyclerView
                        = (RecyclerView)findViewById(
                        R.id.recyclerView);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(
                        new LinearLayoutManager(chatPage.this));
                adapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent=new Intent(chatPage.this,chatRoom.class);
                        intent.putExtra("UserId",Allreceiver.get(position+1));
                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(chatPage.this,"hmmm error",Toast.LENGTH_LONG).show();
            }
        });
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(chatPage.this,contacts.class);
                startActivity(intent);
            }
        });

    }
}



