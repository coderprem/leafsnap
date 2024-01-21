package com.leafsnap;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class user_profile_edit_activity extends AppCompatActivity {

    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_edit_activity);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        findViewById(R.id.edit_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_edit_save();
            }
        });

        ((ImageView)findViewById(R.id.user_photo_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                profile_img_edit_dialog imgDialog = new profile_img_edit_dialog();
                imgDialog.showBottomDialog(user_profile_edit_activity.this,guid);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_user_details();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    void user_edit_save() {
        EditText user_name_txt = findViewById(R.id.user_name_txt);
        EditText user_id_txt = findViewById(R.id.user_id_txt);
        EditText user_bio_txt = findViewById(R.id.user_bio_txt);

        if (user_name_txt.getText().toString().length() < 1) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }

        if (user_id_txt.getText().toString().length() < 5) {
            Toast.makeText(this, "Enter Userid", Toast.LENGTH_SHORT).show();
        }

        else {
            Map<String, Object> mapdata = new HashMap<>();
            mapdata.put("name", user_name_txt.getText().toString());
            mapdata.put("userid", user_id_txt.getText().toString());
            mapdata.put("bio", user_bio_txt.getText().toString());
            mapdata.put("update_date_time", FieldValue.serverTimestamp());
            CollectionReference walletcal = db.collection("account");
            walletcal.document(guid).update(mapdata).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                   onBackPressed();
                    //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
    }
    }
    void get_user_details(){

        DocumentReference docIdRef = db.collection("account").document(guid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot s = task.getResult();
                    if (s.exists()) {
                        if (s.getString("photo")!= null) {
                            ImageView user_img = findViewById(R.id.user_photo_img);
                            Picasso.get().load(s.getString("photo")).into(user_img);
                        }
                        ((TextView)findViewById(R.id.user_name_txt)).setText(s.getString("name"));
                        ((TextView)findViewById(R.id.user_id_txt)).setText(s.getString("userid"));
                        if (s.getString("bio")!="bio") {
                            ((TextView)findViewById(R.id.user_bio_txt)).setText(s.getString("bio"));
                            findViewById(R.id.user_bio_txt).setVisibility(View.VISIBLE);
                        }

                    } else {

                    }
                }
            }
        });

    }
}