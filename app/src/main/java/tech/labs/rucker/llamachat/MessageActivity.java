package tech.labs.rucker.llamachat;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MessageActivity extends AppCompatActivity {

    Button sendBtn;
    TextView sentMessage;
    EditText messageText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        sendMessage();
        dbListen();
    }
    public void sendMessage(){
        sentMessage = (TextView) findViewById(R.id.message);
        messageText = (TextInputEditText) findViewById(R.id.textInput);
        sendBtn = (Button)findViewById(R.id.sendBtn);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference messageRecipient;
                String message = messageText.getText().toString();
                 messageRecipient = database
                        .getReference("Cat")
                        .child("messages")
                        .push();
                messageRecipient.setValue(message);
               final String clientSideKey = messageRecipient.getKey();
                Log.d("Message Key:", clientSideKey);
                messageRecipient.addChildEventListener(new ChildEventListener() {
                    //@Override
                    public void onDataChange(DataSnapshot dataSnapshot ) {
                        String value = dataSnapshot.getValue().toString();//.getChildren().toString(); //.getValue(String.class);
                        for (DataSnapshot questionSnapshot : dataSnapshot.getChildren()) {
//The key of the question
                            String questionKey = questionSnapshot.getKey();
                            Log.d("KEY TAG:", questionKey);

//And if you want to access the rest:
                            ;//String ans1 = questionSnapshot.child(clientSideKey).getValue(String.class);
                            String ans2 = questionSnapshot.child(clientSideKey).getValue(String.class);
                            Log.d("KEY TAG0:", ans2);
                        }
                        //DataSnapshot msgSnapshot = dataSnapshot.getChildren();
                           // String msg = dataSnapshot.("-L4X9y4l0Do2oiEcNpdF").getValue(String.class);
//                            Log.d("TAG:", msg);
//                            sentMessage.setText(msg);



                    }

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Ur fucked", "So fucked");
                    }
                });
            }
        });


    }

    public void dbListen(){
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference messageLog = database.getReference("Cat").child("messages");
//        messageLog.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot ) {
//                String value = dataSnapshot.getValue().toString();//.getChildren().toString(); //.getValue(String.class);
//                sentMessage.setText(value);
//                Log.d("TAG:", value);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d("Ur fucked", "So fucked");
//            }
//        });
    }

}
