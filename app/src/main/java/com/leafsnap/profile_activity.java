package com.leafsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class profile_activity extends AppCompatActivity {
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;
String plant_user_guid;
    SwipeRefreshLayout swipe_layout;
    RecyclerView plant_recyclerView;

    String followed_guid=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        plant_user_guid=getIntent().getStringExtra("plant_user_guid");
        ((ImageView) findViewById(R.id.menu_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                user_profile_menu_dialog menuDialog = new user_profile_menu_dialog();
                menuDialog.showBottomDialog(profile_activity.this,guid);
            }
        });
        findViewById(R.id.follow_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                follow(followed_guid);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_user_details();
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

                        followed_guid=s.getString("guid");
                        find_following(followed_guid);
                        ((TextView)findViewById(R.id.profile_username_txt)).setText("@"+ s.getString("userid"));
                        ((TextView)findViewById(R.id.user_name_txt)).setText(s.getString("name"));
                        ((TextView)findViewById(R.id.user_id_txt)).setText("@"+s.getString("userid"));
                        if (s.getString("bio")!="bio") {
                            ((TextView)findViewById(R.id.user_bio_txt)).setText(s.getString("bio"));
                            findViewById(R.id.user_bio_txt).setVisibility(View.VISIBLE);
                        }


                        //get plant data
                        get_profile_plant_list();
                    } else {

                    }
                }
            }
        });

    }
    void get_profile_plant_list(){
        plant_recyclerView = findViewById(R.id.plant_recyclerView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        plant_recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        profile_plant_list_adapter wAdapter = new profile_plant_list_adapter(dataholder, this);

        CollectionReference collectionReference = db.collection("plants");
        collectionReference.whereEqualTo("status",true).whereEqualTo("guid",plant_user_guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("photo"),
                                    s.getString("plant_name"),
                                    String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),
                                    s.getString("plant_loc"),
                                    String.valueOf(s.getDouble("plant_streak").intValue()),
                                    "",
                                    "",
                                    "",
                                    "",
                                    s.getString("reg_uid"));

                            dataholder.add(obj);
                        }
                        plant_recyclerView.setAdapter(wAdapter);
                    }
                });
    }
    private void find_following(String followed_guid) {
        CollectionReference collectionReference = db.collection("account").document(guid).collection("follow");
        collectionReference.whereEqualTo("followed_guid",followed_guid)

                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        for (QueryDocumentSnapshot s : task.getResult()){
                            ((TextView)findViewById(R.id.follow_txt)).setText("Following");

                      }

            }else {
                        ((TextView)findViewById(R.id.follow_txt)).setText("Follow");
                    }
        });

    }

    void follow(String followed_guid){
        CollectionReference collectionReference = db.collection("account").document(guid).collection("follow");
        collectionReference.whereEqualTo("followed_guid",followed_guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        db.collection("account").document(guid).collection("follow").document(followed_guid)
                                .delete().addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                ((TextView) findViewById(R.id.follow_txt)).setText("Follow");
                            }
                        });
                    } else {

                        Map<String, Object> mapdata = new HashMap<>();
                        mapdata.put("uid", Utils.randomcode(13));
                        mapdata.put("guid", guid);
                        mapdata.put("followed_guid", followed_guid);
                        mapdata.put("date_time", FieldValue.serverTimestamp());
                        CollectionReference CRefedata = db.collection("account").document(guid).collection("follow");
                        CRefedata.document(followed_guid).set(mapdata).addOnCompleteListener(task2 -> {
                            if (task2.isSuccessful()) {
                                ((TextView) findViewById(R.id.follow_txt)).setText("Following");
                            }
                        });
                    }
                });
    }
}