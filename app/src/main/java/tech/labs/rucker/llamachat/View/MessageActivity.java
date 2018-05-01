package tech.labs.rucker.llamachat.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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

    // Todo: BUG -- all rooms only display first message.
    // Todo: Delete room from list with long-press and/or button.
    // Todo: Check for empty-text room. Text input should not be empty.
    // Todo: Hide Keyboard on Send
    // Todo: Scroll to bottom of List Automatically

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

    FloatingActionButton sendBtn;
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
    public String checkIntent(){

           String roomName = getIntent().getStringExtra("ROOM_NAME");
            Log.d("String EXTRA", roomName);
           return roomName;

    }

    public void initMessage(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final String name = mAuth.getCurrentUser().getDisplayName();
        final String uId = mAuth.getCurrentUser().getUid();
        DatabaseReference messageRecipient;

        String roomName = getIntent().getStringExtra("ROOM_NAME");

        messageRecipient = database
                .getReferenceFromUrl("https://llamachat-a4865.firebaseio.com/Rooms/" + roomName);

        final String msgKey = messageRecipient.push().getKey();
        final String clientSideKey = messageRecipient.getKey();

        messageRecipient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems = new ArrayList<>();
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                    String msg0 = msgSnapshot.getValue().toString();
                    ListItem listItem = new ListItem(" ", msg0);
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


    public void sendMessage(final String contactId){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();


        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final DatabaseReference messageRecipient;
                final DatabaseReference messageSender;

                FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference;


                final String name = mAuth.getCurrentUser().getDisplayName();
                final String message = name + ": \n" + messageText.getText().toString();
                //final String uId = mAuth.getUid();

                final String uId = mAuth.getCurrentUser().getUid();

                String urlPath = "https://llamachat-a4865.firebaseio.com/Rooms/" + checkIntent();
                // Reference to room path
                messageRecipient = database
                        .getReferenceFromUrl("https://llamachat-a4865.firebaseio.com/Rooms/" + checkIntent());

                databaseReference = databaseInstance.getReferenceFromUrl(urlPath);

                final String msgKey = messageRecipient.push().getKey();

                // Actually sends the message
                final String msgmsg = messageRecipient.push().setValue(message).toString();
                final String clientSideKey = messageRecipient.getKey();
                final String clientSideKeyRecipient = messageRecipient.getKey();

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        listItems = new ArrayList<>();
                        for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                            String msg0 = msgSnapshot.getValue().toString();
                            ListItem listItem = new ListItem(" ", msg0);
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



}
