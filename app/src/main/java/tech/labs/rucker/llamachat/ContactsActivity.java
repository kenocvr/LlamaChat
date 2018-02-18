package tech.labs.rucker.llamachat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ContactsActivity extends AppCompatActivity {

    // Todo: Create User Profile. Choose Display Name.
    // Todo: Create user contacts model and layout
    // Todo: Create Nav Drawer Activity for Contacts
    // Todo: Floating action bar to create new message
    // Todo: Nav Drawer to manage account and settings

    Button button;
    Button messageBtn;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

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
        messageBtn = findViewById(R.id.messageBtn);
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
}
