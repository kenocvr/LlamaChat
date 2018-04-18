package tech.labs.rucker.llamachat.View;

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

import tech.labs.rucker.llamachat.Controller.MessageAdapter;
import tech.labs.rucker.llamachat.Model.ListItem;
import tech.labs.rucker.llamachat.R;

public class MessageActivity extends AppCompatActivity {

    // Todo: Receive extras from "room" selection in Contacts recyclerView
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
    private String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        FirebaseAuth user = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference(user.getUid()).child("messages");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        checkIntent();
        sentMessage = findViewById(R.id.message);
        messageText = (TextInputEditText) findViewById(R.id.textInput);
        sendBtn = findViewById(R.id.sendBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        initMessage();
        // Hard Coded Test User //
        sendMessage("room");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView. setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    public void checkIntent(){
        if(getIntent().hasExtra("ROOM_NAME")){
            String roomName = getIntent().getStringExtra("ROOM_NAME");
        }

    }


    public String displayUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user.getDisplayName();
    }
    public String displayUserId(){
        FirebaseAuth userId = FirebaseAuth.getInstance();
        return userId.toString();
    }
    public void initMessage(){
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String name = mAuth.getCurrentUser().getDisplayName();
        final String uId = mAuth.getCurrentUser().getUid();
        final DatabaseReference messageRecipient;
        String roomName = getIntent().getStringExtra("ROOM_NAME");
//        messageRecipient = database
//                .getReference(uId)
//                .child("messages");
        messageRecipient = database
                .getReference(roomName);

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
                    Log.d("Message Data::",msg0);
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
                //final String uId = mAuth.getUid();
                final String name = mAuth.getCurrentUser().getDisplayName();
                final String uId = mAuth.getCurrentUser().getUid();

                messageRecipient = database
                        .getReference(uId)
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
                                Log.d("Message Data::",msg0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    public void sendMessage(final String contactId){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatabaseReference messageRecipient;
                final DatabaseReference messageSender;
                final String name = mAuth.getCurrentUser().getDisplayName();
                final String message = name + ": " + messageText.getText().toString();
                //final String uId = mAuth.getUid();

                final String uId = mAuth.getCurrentUser().getUid();

                // Database Message Symmetry
                // Users in conversation receive the same messages
                // Creates Chat Room based on string parameter
                messageRecipient = database
                        .getReference(contactId);

//                messageRecipient = database
//                        .getReference(uId)
//                        .child(contactId);
//
//                messageSender = database
//                        .getReference(contactId)
//                        .child(uId + contactId);

                final String msgKey = messageRecipient.push().getKey();

                // Actually sends the message
                final String msgmsg = messageRecipient.push().setValue(message).toString();

                final String clientSideKey = messageRecipient.getKey();
                final String clientSideKeyRecipient = messageRecipient.getKey();

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
                            Log.d("Message Data::",msg0);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                messageSender.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        listItems = new ArrayList<>();
//                        for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
//                            String msg0 = msgSnapshot.getValue().toString();
//                            ListItem listItem = new ListItem(name, msg0);
//                            listItems.add(listItem);
//
//                            adapter = new MessageAdapter(listItems, MessageActivity.this);
//                            recyclerView.setAdapter(adapter);
//                            Log.d("ASDFASFGAS::",msg0);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });

            }
        });


    }



}
