package tech.labs.rucker.llamachat;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {

    Button sendBtn;
    TextView sentMessage;
    EditText messageText;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        sendMessage();
    }
    public void sendMessage(){
        sentMessage = (TextView) findViewById(R.id.message);
        messageText = (TextInputEditText) findViewById(R.id.textInput);
        sendBtn = (Button)findViewById(R.id.sendBtn);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
       // String dummy = "aaa";

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageText.getText().toString();
                sentMessage.setText(message);
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue(message);
            }
        });
        //
       // helloMsg.setText("Hello, World!");



    }
}
