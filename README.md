# LlamaChat

A chat app for Android. Uses Firebase Realtime Database and one touch sign in with Google Authentication. 

# Setup
### Add Firebase to your project
* [https://firebase.google.com/docs/android/setup](https://firebase.google.com/docs/android/setup)

---
### Add google-services.json file
* Download **google-services.json** from the settings menu in firebase console.

* Copy the file to the correct app module project directory.

---
# User Authentication with Google Sign-In
* [https://firebase.google.com/docs/auth/android/google-signin](https://firebase.google.com/docs/auth/android/google-signin)

 ### Authenticating Your Client and getting your app's SHA-1 fingerprint.
* [https://developers.google.com/android/guides/client-auth](https://developers.google.com/android/guides/client-auth)

##### FirebaseAuth class is the entry point of the Firebase Authentication SDK.

The class is instanciated by calling getInstance().
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ...
        mAuth = FirebaseAuth.getInstance();
        ...
     }
```
 ---
 ### The AuthSateChanged method gets invoked on the main thread:
* Right after the listener has been registered
* When a user is signed in
* When the current user is signed out
* When the current user changes
 ```java
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null){
                    startActivity(new Intent(SignInActivity.this, ContactsActivity.class));
                }
            }
        };
 ```
 ---
 ### Always authenticate by passing in the token using the method: getIdToken
 ```java
   private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                          , Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                    }
                });
    }
