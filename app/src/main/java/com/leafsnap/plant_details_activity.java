package com.leafsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class plant_details_activity extends AppCompatActivity {

    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    String plant_user_guid;
    String reg_uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plant_details_activity);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);

        address_loc = sharedPreferences.getString(login_activity.address_loc_KEY, null);
        reg_uid=getIntent().getStringExtra("reg_uid");
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

TextView delete_btn_txt= findViewById(R.id.delete_btn_txt);
        findViewById(R.id.delete_btn_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox = findViewById(R.id.delete_check_box);
                if (checkBox.isChecked()){
                    delete_btn_txt.setText("Deleting...");
                    CollectionReference collectionOfProducts = db.collection("plants");
                    collectionOfProducts.whereEqualTo("guid",guid).whereEqualTo("reg_uid",reg_uid)
                            .get().addOnCompleteListener(task -> {
                                if (task.isSuccessful() && task.getResult().size()>0) {
                                    for (QueryDocumentSnapshot s : task.getResult()) {
                                        db.collection("plants").document(s.getId())
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                            //Snackbar.make(findViewById(android.R.id.content), "Delete Successfully", Snackbar.LENGTH_SHORT).show();
                                                            delete_btn_txt.setText("Delete Successfully");
                                                            onBackPressed();

                                                    }
                                                });
                                    }
                                }else
                                {
                                    Snackbar.make(findViewById(android.R.id.content), "Not find", Snackbar.LENGTH_SHORT).show();
                                    delete_btn_txt.setText("Delete");
                                }
                            });
                }
            }
        });

        findViewById(R.id.username_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(plant_details_activity.this, profile_activity.class);
                intent.putExtra("plant_user_guid", plant_user_guid);
                 startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        get_plant_details();
    }

    void get_user_details(){

        DocumentReference docIdRef = db.collection("account").document(plant_user_guid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot s = task.getResult();
                    if (s.exists()) {
                        ImageView user_img=findViewById(R.id.user_photo_img);
                        Picasso.get().load(s.getString("photo")).into(user_img);

                        ((TextView)findViewById(R.id.username_txt)).setText("@"+ s.getString("userid"));
                        //((TextView)findViewById(R.id.user_name_txt)).setText(s.getString("name"));
                       // ((TextView)findViewById(R.id.user_id_txt)).setText("@"+s.getString("userid"));
//                        if (s.getString("bio")!="bio") {
//                            ((TextView)findViewById(R.id.user_bio_txt)).setText(s.getString("bio"));
//                            findViewById(R.id.user_bio_txt).setVisibility(View.VISIBLE);
//                        }


                        //get plant data

                    } else {

                    }
                }
            }
        });

    }
    void get_plant_details(){

        SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        CollectionReference collectionReference = db.collection("plants");
        collectionReference.whereEqualTo("reg_uid",reg_uid).whereEqualTo("status",true)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){

                        for (QueryDocumentSnapshot s : task.getResult()){
                            plant_user_guid=s.getString("guid");
                            get_user_details();
                            ImageView plant_img=findViewById(R.id.plant_img);
                            Picasso.get().load(s.getString("photo")).into(plant_img);
                            if (s.getString("guid").equals(guid)) {
                                ImageView fav_plant_img = findViewById(R.id.favorite_btn);
                                fav_plant_img.setVisibility(View.GONE);
                            }
                            ((TextView)findViewById(R.id.plant_name_txt)).setText(s.getString("plant_name"));
                            ((TextView)findViewById(R.id.plant_loc_txt)).setText(s.getString("plant_loc"));
                            ((TextView)findViewById(R.id.like_count_txt)).setText(String.valueOf(s.getDouble("like").intValue()+" likes"));
                            ((TextView)findViewById(R.id.comment_count_txt)).setText(String.valueOf(s.getDouble("comment").intValue()+ " comments"));
                            ((TextView)findViewById(R.id.share_count_txt)).setText(String.valueOf(s.getDouble("share").intValue()+" shares"));

                            ((TextView)findViewById(R.id.plant_dob_txt)).setText(String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())));
                            ((TextView)findViewById(R.id.plant_public_note_txt)).setText(s.getString("plant_note"));
                            ((TextView)findViewById(R.id.plant_common_name_txt)).setText(s.getString("common_name"));
                            ((TextView)findViewById(R.id.plant_scientific_name_txt)).setText(s.getString("scientific_name"));
                            ((TextView)findViewById(R.id.plant_family_txt)).setText(s.getString("family"));
                            ((TextView)findViewById(R.id.plant_genus_txt)).setText(s.getString("genus"));
                            ((TextView)findViewById(R.id.plant_weekly_targets_txt)).setText(String.valueOf(s.getDouble("plant_target").intValue()));
                            ((TextView)findViewById(R.id.plant_streak_txt)).setText(String.valueOf(s.getDouble("plant_streak").intValue()));
                     }

                    }else {
                        Toast.makeText(this, "this not available", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}