package com.leafsnap.administrator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafsnap.R;
import com.leafsnap.login_activity;
import com.leafsnap.view_model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class task_list_activity extends AppCompatActivity {
    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    //location variables
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    SwipeRefreshLayout swipe_layout;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);
        //
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);
        findViewById(R.id.create_task_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(task_list_activity.this, task_create_activity.class));
            }
        });

        swipe_layout = findViewById(R.id.swipe_layout);
        recyclerView = findViewById(R.id.recycler_view);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
              get_task_list();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        get_task_list();
    }

    void get_task_list(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();
        task_list_adapter wAdapter = new task_list_adapter(dataholder, this);
        CollectionReference collectionReference = db.collection("task");
        collectionReference
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("task"),
                                    s.getString("category"),
                                    String.valueOf(s.getDouble("point").intValue()),
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    String.valueOf(dateFormat.format(s.getTimestamp("date_time").toDate())),
                                    s.getString("task_uid"));

                            dataholder.add(obj);
                        }
                        recyclerView.setAdapter(wAdapter);
                        swipe_layout.setRefreshing(false);
                    }else
                    {

                    }
                });
    }
}