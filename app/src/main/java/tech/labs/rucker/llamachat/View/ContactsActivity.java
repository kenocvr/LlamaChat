package tech.labs.rucker.llamachat.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tech.labs.rucker.llamachat.Controller.ContactsAdapter;
import tech.labs.rucker.llamachat.Controller.DatabaseHelper;
import tech.labs.rucker.llamachat.Model.ListItem;
import tech.labs.rucker.llamachat.R;

public class ContactsActivity extends AppCompatActivity {

    EditText roomEditText;
    Button button;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private String value;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private DatabaseReference mRef;


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        button = findViewById(R.id.signOut);
        roomEditText = findViewById(R.id.roomEditText);
        recyclerView = findViewById(R.id.contactsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsView();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() ==null) {
                    startActivity(new Intent(ContactsActivity.this, SignInActivity.class));
                }
            }
        };

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

    }
    public void contactsView(){
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        String urlPath = "https://llamachat-a4865.firebaseio.com/cFTFT9wNF9aLRpijxpmuncTbETt2/Rooms";
        databaseReference = databaseInstance.getReferenceFromUrl(urlPath);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItems = new ArrayList<>();
                for (DataSnapshot msgSnapshot : dataSnapshot.getChildren()){
                    String roomName = msgSnapshot.getValue().toString();
                    ListItem listItem = new ListItem("room:", roomName);
                    listItems.add(listItem);
                    adapter = new ContactsAdapter(listItems, ContactsActivity.this);
                    recyclerView.setAdapter(adapter);
                    Log.d("Message Data::",roomName);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void addRoom(View view) {
        String roomStr = roomEditText.getText().toString();
        DatabaseHelper roomName = new DatabaseHelper();
        roomName.setParentNode("parentRm").setChildNode("childRm").setChildValue(roomStr).push();
        roomName.setParentNode("Rooms").setChildNode(roomStr).setChildValue("Welcome to " + roomStr + "!").push();
        DatabaseHelper usersRooms = new DatabaseHelper();
        DatabaseHelper roomMessages = new DatabaseHelper();
        // User's rooms
        usersRooms.setParentNode(mAuth.getUid()).setChildNode("Rooms").setChildValue(roomStr).push();
        // Creates room with welcome message
        roomMessages.setParentNode("Rooms").setChildNode(roomStr).setChildValue("Welcome to " + roomStr + "!");
    }

}
