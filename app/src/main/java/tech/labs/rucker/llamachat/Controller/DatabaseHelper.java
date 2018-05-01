package tech.labs.rucker.llamachat.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Carlos on 3/24/2018.
 */

    //  User Rooms schema. //
// UserID:
//     Rooms:
//          ****: "myRoom1"
//          ****: "myRoom2"
    
    // Rooms Messages  //
// Rooms:
//      myRoom1:
//          ****: "displayName says: first message"
//          ****: "displayName says: second message"


public class DatabaseHelper {
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mRef;

    private String message;
    private String parentNode;
    private String childNode;
    private String childValue;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParentNode() {
        return parentNode;
    }

    public String getChildNode() {
        return childNode;
    }

    public String getChildValue() {
        return childValue;
    }

    public DatabaseHelper(){

    }

    public DatabaseHelper setParentNode(String parentNode){
        this.parentNode = parentNode;
        return this;
    }

    public DatabaseHelper setChildNode(String childNode){
        this.childNode = childNode;
        return this;
    }

    public DatabaseHelper setChildValue(String childValue){
        this.childValue = childValue;
        return this;
    }
    public void pushRoot(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db;
        db = database.getReference(getParentNode());
        String msgmsg = db
                .setValue(getChildValue())
                .toString();
        String msgkey = db.getKey();
    }
    public void push(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db;
        db = database.getReference(getParentNode()).child(getChildNode()).push();
        String msgmsg = db
                .setValue(getChildValue())
                .toString();
        String msgkey = db.getKey();
    }
    public void pushTwo(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db;
        db = database.getReference(getParentNode()).child(getChildNode()).push();
        String msgmsg = db
                //.setValue(getChildValue())
                .toString();
        String msgkey = db.getKey();
    }

}
