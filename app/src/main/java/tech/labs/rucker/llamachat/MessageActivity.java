package tech.labs.rucker.llamachat;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    // Todo: Hide Keyboard on Send
    // Todo: Fix Input field Overlap in UI
    // Todo: Scroll to bottom of List Automatically

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

    Button sendBtn;
    TextView sentMessage;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mRef = FirebaseDatabase.getInstance().getReference("Cat").child("messages");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        sentMessage = findViewById(R.id.message);
        messageText = (TextInputEditText) findViewById(R.id.textInput);
        sendBtn = findViewById(R.id.sendBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        initMessage();
        sendMessage();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView. setHasFixedSize(true);
        //recyclerView.scrollToPosition(listItems.size());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    public String displayUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }

    public void initMessage(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String name = mAuth.getCurrentUser().getDisplayName();
        final DatabaseReference messageRecipient;
        messageRecipient = database
                .getReference(name)
                .child("messages");

        final String msgKey = messageRecipient.push().getKey();
        final String clientSideKey = messageRecipient.getKey();

        messageRecipient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems = new ArrayList<>();
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                    String msg0 = msgSnapshot.getValue().toString();
                    ListItem listItem = new ListItem(name, msg0);
                    listItems.add(listItem);
                    adapter = new MessageAdapter(listItems, MessageActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.d("ASDFASFGAS::",msg0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void sendMessage(){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference messageRecipient;
                final String message = messageText.getText().toString();
                final String uId = mAuth.getUid();
                final String name = mAuth.getCurrentUser().getDisplayName();
                messageRecipient = database
                        .getReference(name)
                        .child("messages");
                 final String msgKey = messageRecipient.push().getKey();
                 final String msgmsg = messageRecipient.push().setValue(message).toString();
                 final String clientSideKey = messageRecipient.getKey();

               messageRecipient.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listItems = new ArrayList<>();
                        for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                            String msg0 = msgSnapshot.getValue().toString();
                            ListItem listItem = new ListItem(name, msg0);
                            listItems.add(listItem);

                            adapter = new MessageAdapter(listItems, MessageActivity.this);
                            recyclerView.setAdapter(adapter);
                                Log.d("ASDFASFGAS::",msg0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }



}
