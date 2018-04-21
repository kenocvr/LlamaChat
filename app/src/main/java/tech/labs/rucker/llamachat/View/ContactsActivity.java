package tech.labs.rucker.llamachat.View;

import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseUser;
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


    // Todo(1): Item click listener... myRoomStr.OnClickListener => Intent/Extras => MessageActivity

        /*
        *   ChatRoom Schema
        * */
        /*
        *User rooms -- Display in client side RecyclerView/list
        * */
    //(ROOT)userID:
        // Rooms:
            // *****: myRoomStr0
            // *****: myRoomStr1
        /*
        * Rooms with chat messages. Root Level. All clients can add(read) and write to.
        * */
    //(ROOT)Rooms:
        // myRoomStr0:
            // ******: "Carlos says: Doood! The Chat room works!!!"
            // ******: "D says: ikr, so cool!"

        // myRoomStr1:

        // herRoomStr4: "No one knows about this room yet!"

        // Edit Text
        // Firebase Database ⇒ push() ⇒  UserID.rooms

    EditText roomEditText;
    Button button;
    Button messageBtn;
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
        //writeUserToList("Users");

        button = findViewById(R.id.signOut);
        messageBtn = findViewById(R.id.messageBtn);
        roomEditText = findViewById(R.id.roomEditText);
        recyclerView = findViewById(R.id.contactsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactsView();
        //addUserToList();
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

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactsActivity.this, MessageActivity.class));
            }
        });


    }
    public void contactsView(){
        FirebaseDatabase databaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference;

        //String userId = mAuth.getUid();
        String urlPath = "https://llamachat-a4865.firebaseio.com/cFTFT9wNF9aLRpijxpmuncTbETt2/Rooms";
        //"https://llamachat-a4865.firebaseio.com/" + userId + "/Rooms";
        // Get reference to User's rooms list in:
        // userId: rooms: ...
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

                    // Todo: Clicking chat room item. Pass extras: roomName, etc
                    // Follow pattern: https://android.jlelse.eu/click-listener-for-recyclerview-adapter-2d17a6f6f6c9
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
//    public void writeToDatabase(String parent, String child, String value){
//
//        DatabaseHelper roomName = new DatabaseHelper();
//        roomName.setParentNode("parentRoom").setChildNode("childRoom").setChildValue("valueRoom");
//
//
//        //.value = value;
//       // final FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        //final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        //final DatabaseReference db;
//       // db = database.getReference(parent).child(child);
//       // final String msgmsg = db.push()
//        //        .setValue(value)
//        //        .toString();
//       // final String msgkey = db.getKey();
//    }

    public void writeUserToList(final String param){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference messageRecipient;
                messageRecipient = database
                        .getReference()
                        .child(param);
        final String messageRecipientStr = messageRecipient.getKey();
        final String msgmsg = messageRecipient.push()
                .setValue(mAuth.getCurrentUser()
                        .getDisplayName())
                        .toString();
    }

    public void addUserToList(){
        // Get and Write current user to list of users
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mRef;
        mRef = database
                .getReference("users")
                .child("TESTEMAIL");
        final String clientSideKey = mRef.getKey();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
        }
    }
//    public void initView(){
//        listItems = new ArrayList<>();
//        for(int i = 0; i <=10; i++){
//            ListItem listItem = new ListItem(
//                    "heading" +(i+1),
//                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
//            );
//            listItems.add(listItem);
//        }
//
//        adapter = new ContactsAdapter(listItems, ContactsActivity.this);
//        recyclerView.setAdapter(adapter);
//    }

    public void addRoom(View view) {
        String roomStr = roomEditText.getText().toString();
        DatabaseHelper roomName = new DatabaseHelper();

        // Todo: Duplicate entry check.
        // Check if entry does not exist in database
        // if (entry == null){ roomName...push(); }
        roomName.setParentNode("parentRm").setChildNode("childRm").setChildValue(roomStr).push();
        roomName.setParentNode("Rooms").setChildNode(roomStr).setChildValue("Welcome!").push();
        DatabaseHelper usersRooms = new DatabaseHelper();
        DatabaseHelper roomMessages = new DatabaseHelper();
        // Todo: RecyclerView based on these room values.
        // User's rooms
        usersRooms.setParentNode(mAuth.getUid()).setChildNode("Rooms").setChildValue(roomStr).push();
        // Todo: Check if room exists. Append messages. Avoid deleting messages with blank room creation.
        // If room != null, initialize room with welcome message.
        // //JSON tree requires children with messages
        // Chat room schema //
        // Rooms
        // <roomStr>
        // ****:<message>
        // ****:<message>

        // Creates room with welcome message
        roomMessages.setParentNode("Rooms").setChildNode(roomStr).setChildValue("Welcome!");
    }

}
