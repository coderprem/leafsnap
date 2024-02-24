package com.leafsnap.Community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafsnap.R;
import com.leafsnap.login_activity;
import com.leafsnap.plant_details_activity;
import com.leafsnap.profile_activity;
import com.leafsnap.view_model;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class posts_activity extends AppCompatActivity {
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    SwipeRefreshLayout swipe_layout;
    RecyclerView post_recyclerView;
    String post_user_guid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posts_activity);

        sharedPreferences = this.getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        post_user_guid= getIntent().getStringExtra("guid");
        swipe_layout = findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_post_list();

            }
        });

//        findViewById(R.id.username_txt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(posts_activity.this, profile_activity.class);
//                intent.putExtra("post_user_guid", post_user_guid);
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();
        get_post_list();
    }


    void get_post_list(){
        post_recyclerView = findViewById(R.id.post_recycler_view);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(this);
        post_recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        posts_list_adapter wAdapter = new posts_list_adapter(dataholder, this);

        CollectionReference collectionReference = db.collection("posts");
        collectionReference.whereEqualTo("status",true)//.whereEqualTo("guid",post_user_guid)
                //.orderBy("date_time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot s : task.getResult()){
                            db.collection("account").document(s.getString("guid")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                    if (task2.isSuccessful()) {
                                        DocumentSnapshot s2 = task2.getResult();
                                        if (s2.exists()) {
                                    Boolean  follow=true;
                                    if (s.getString("guid").equals(guid))
                                    {
                                        follow=false;
                                    }
                            view_model obj = new view_model(
                                    s.getString("photo"), //1
                                    s.getString("caption"),//2
                                   s2.getString("userid"), //3
                                   s2.getString("photo"), //4
                                    //String.valueOf(s.getDouble("plant_streak").intValue()),
                                    String.valueOf(follow),//5
                                    "", //6
                                    "", //7
                                    "", //8
                                    String.valueOf(dateFormat.format(s.getTimestamp("date_time").toDate())), //9
                                    s.getString("post_uid")); //10

                            dataholder.add(obj);
                                        }
                                        post_recyclerView.setAdapter(wAdapter);
                                    }

                                }
                            });
                        }

                       // get_user_details();

                    }
                });
    }
}