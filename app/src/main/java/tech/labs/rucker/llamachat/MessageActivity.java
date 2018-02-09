package tech.labs.rucker.llamachat;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    // Todo: Attach messages to RecyclerView

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
        sentMessage = (TextView) findViewById(R.id.message);
        messageText = (TextInputEditText) findViewById(R.id.textInput);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        sendMessage();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView. setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        for(int i = 0; i <=10; i++){
            ListItem listItem = new ListItem(
                    "heading" +(i+1),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            );
            listItems.add(listItem);
        }

        adapter = new MessageAdapter(listItems, this);
        recyclerView.setAdapter(adapter);
    }
    public void sendMessage(){


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference messageRecipient;
                final String message = messageText.getText().toString();
                 messageRecipient = database
                        .getReference("Cat")
                        .child("messages");

                 final String msgKey = messageRecipient.push().getKey();

                final String msgmsg = messageRecipient.push().setValue(message).toString();
                //messageRecipient.setValue(message); // <====
               final String clientSideKey = messageRecipient.getKey();

               messageRecipient.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                            String msg0 = msgSnapshot.getValue().toString();
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
