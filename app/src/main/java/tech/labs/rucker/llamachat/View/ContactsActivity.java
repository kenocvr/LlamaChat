package tech.labs.rucker.llamachat.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import tech.labs.rucker.llamachat.ContactsAdapter;
import tech.labs.rucker.llamachat.Model.ListItem;
import tech.labs.rucker.llamachat.R;

public class ContactsActivity extends AppCompatActivity {

    // Todo: Get current user email during onCreate. Write user to 'users' list in database
    // Todo: Add RecyclerView with mock contacts
    // Todo: Create Add Contact and-or Search Contact
    // Todo: Create Nav Drawer Activity for Contacts
    // Todo: Floating action bar to create new message
    // Todo: Nav Drawer to manage account and settings

    Button button;
    Button messageBtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
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
        writeUserToList("Users");
        button = findViewById(R.id.signOut);
        messageBtn = findViewById(R.id.messageBtn);
        recyclerView = findViewById(R.id.contactsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initView();
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
    public void writeUserToList(final String userTest){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference messageRecipient;
                messageRecipient = database
                        .getReference()
                        .child(userTest);
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
    public void initView(){
        listItems = new ArrayList<>();
        for(int i = 0; i <=10; i++){
            ListItem listItem = new ListItem(
                    "heading" +(i+1),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            );
            listItems.add(listItem);
        }

        adapter = new ContactsAdapter(listItems, ContactsActivity.this);
        recyclerView.setAdapter(adapter);
    }
}
