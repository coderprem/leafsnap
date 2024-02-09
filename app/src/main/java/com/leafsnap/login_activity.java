package com.leafsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login_activity extends AppCompatActivity {
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    public static final String SHARED_PREFS = "leafsnap_prefs";
    Animation bottomUp, bottomdown, AnimationIN, AnimationOUT;
    SharedPreferences sharedpreferences;
    public static final String guid_sip_key = "guid_sip_type";
    public static final String account_type_key = "account_type";
    public static final String login_photo_KEY = "aguid";
    public static final String Guid_KEY = "guid";
    public static final String token_KEY = "token";
    public static final String loginname_KEY = "loginname";
    public static final String loginkey_KEY = "login";
    public static final String Timezone_KEY = "timezone";
    public static final String letlong_KEY = "letlong";
    public static final String email_id_KEY = "email";
    private static final int RESOLVE_HINT = 120;
    //location
    String latitude = null,longitude = null, address_loc = null,address=null,city=null,postalCode=null,state=null,country=null,code=null,flag=null,ip=null,device=null,device_mic=null;

    public static final String loc_name_key = "loc_name";
    public static final String latitude_key = "latitude";
    public static final String longitude_key = "longitude";
    public static final String address_loc_KEY = "address_loc";
    public static final String city_key = "city";
    public static final String postalCode_key = "zip";
    public static final String state_key = "state";
    public static final String country_key = "country";
    public static final String code_key = "code";
    public static final String flag_key = "flag";

    public static final String ip_key = "code";
    public static final String device_key = "flag";
    public static final String device_id_key = "flag";
    //
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;
    EditText emailEditText,passwordEditText;
    Button loginBtn;
    ProgressBar progressBar;
    TextView createAccountBtnTextView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        //
        //
//        emailEditText =(EditText) findViewById(R.id.email_edit_text);
//        passwordEditText =(EditText) findViewById(R.id.password_edit_text);
//        loginBtn =(Button) findViewById(R.id.login_btn);
//        progressBar =(ProgressBar) findViewById(R.id.progress_bar);

//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginUser
//                        ();
//            }
//        });
         //google_btn
        ((LinearLayout) findViewById(R.id.google_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
     // FirebaseUser currentUser = mAuth.getCurrentUser();
      // updateUI(currentUser);
    }

//    void loginUser(){
//        String email  = emailEditText.getText().toString();
//        String password  = passwordEditText.getText().toString();
//        boolean isValidated = validateData(email,password);
//        if(!isValidated){
//            return;
//        }
//        loginAccountInFirebase(email,password);
//    }

//    void loginAccountInFirebase(String email,String password){
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        changeInProgress(true);
//        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                changeInProgress(false);
//                if(task.isSuccessful()){
//                    //login is success
//                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
//
//                        updateUI();
//
//                    }else{
//                        Toast.makeText(login_activity.this, "Email not verified, Please verify your email.", Toast.LENGTH_SHORT).show();
//                      //  Utility.showToast(LoginActivity.this,"Email not verified, Please verify your email.");
//                    }
//
//                }else{
//                    //login failed
//                    Toast.makeText(login_activity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//    }
//    void changeInProgress(boolean inProgress){
//        if(inProgress){
//            progressBar.setVisibility(View.VISIBLE);
//            loginBtn.setVisibility(View.GONE);
//        }else{
//            progressBar.setVisibility(View.GONE);
//            loginBtn.setVisibility(View.VISIBLE);
//        }
//    }
//    boolean validateData(String email,String password){
//        //validate the data that are input by user.
//
//        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//            emailEditText.setError("Email is invalid");
//            return false;
//        }
//        if(password.length()<6){
//            passwordEditText.setError("Password length is invalid");
//            return false;
//        }
//        return true;
//    }

   // google_btn
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                           //  FirebaseUser user = mAuth.getCurrentUser();

                           updateUI();

                        } else {
                            Toast.makeText(login_activity.this, "Failure "+ task.getException(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        guid =user.getUid();// Utils.randomnumber9(4)+""+Utils.randomnumber(6)+""+Utils.randomnumber9(2);
        loginkey=Utils.randomcode(12);
        //
        DocumentReference docIdRef = db.collection("account").document(guid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot s = task.getResult();
                    String email = String.valueOf(s.get("email"));
                    if (s.exists() && user.getEmail().equals(email)) {
                        login_retract_activity();
                        token=s.getString("token");
                    } else {
                        signup_retract_activity();
                    }
                }
            }
                });

    }
    private void login_retract_activity()
    {   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loginname = user.getDisplayName().substring(0, 1).toUpperCase() + user.getDisplayName().substring(1).toLowerCase();
        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("otp", Utils.randomnumber(6));
        mapdata.put("loginkey", loginkey);
        mapdata.put("device", android.os.Build.MODEL);
        mapdata.put("device_id", device_id);
       mapdata.put("token", token);
       // mapdata.put("loginname",loginname);
       // mapdata.put("photo", String.valueOf(user.getPhotoUrl()));
       // mapdata.put("Status", "true");
        mapdata.put("login_date_time", FieldValue.serverTimestamp());
        CollectionReference walletcal = db.collection("account");
        walletcal.document(guid).update(mapdata);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedpreferences.edit();
         editor.putString(login_activity.login_photo_KEY, String.valueOf(user.getPhotoUrl()));
         editor.putString(login_activity.Guid_KEY, guid);
        editor.putString(login_activity.email_id_KEY, user.getEmail());
        editor.putString(login_activity.loginname_KEY, loginname);
         editor.putString(login_activity.loginkey_KEY, loginkey);
         editor.putString(login_activity.device_key,android.os.Build.MODEL);
         editor.apply();
        Intent intent = new Intent(login_activity.this, HomeActivity.class);
        startActivity(intent);
       // this.finish();
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
    private void signup_retract_activity()
    {   FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        loginname = user.getDisplayName().substring(0, 1).toUpperCase() + user.getDisplayName().substring(1).toLowerCase();
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(login_activity.login_photo_KEY, String.valueOf(user.getPhotoUrl()));
        editor.putString(login_activity.Guid_KEY, guid);
        editor.putString(login_activity.device_id_key, device_id);
        editor.putString(login_activity.loginname_KEY, loginname);
        editor.putString(login_activity.loginkey_KEY, loginkey);
        editor.putString(login_activity.email_id_KEY, user.getEmail());
        editor.putString(login_activity.device_key,android.os.Build.MODEL);
        editor.apply();
        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("syncid", Utils.randomcode(13));
        mapdata.put("guid", guid);
        mapdata.put("userid", loginname.replace(" ","")+""+Utils.randomcode(6));
        mapdata.put("name", loginname);
        mapdata.put("bio","bio");
        mapdata.put("photo", String.valueOf(user.getPhotoUrl()));
        mapdata.put("mobile", "");
        mapdata.put("code", "");
        mapdata.put("country", "new");
        mapdata.put("email", user.getEmail());
        mapdata.put("otp", Utils.randomnumber(6));
        mapdata.put("device", android.os.Build.MODEL);
        mapdata.put("device_id", device_id);
        mapdata.put("token", token);
        mapdata.put("loginkey", loginkey);
        mapdata.put("Status", "true");
        mapdata.put("login_date_time", FieldValue.serverTimestamp());
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("account");
        CRefedata.document(guid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                Intent intent = new Intent(login_activity.this, HomeActivity.class);
                startActivity(intent);
               // this.finish();
                //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });
    }

}