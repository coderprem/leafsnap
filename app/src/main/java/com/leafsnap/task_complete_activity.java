package com.leafsnap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class task_complete_activity extends AppCompatActivity {
    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    //location variables
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    SwipeRefreshLayout swipe_layout;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;
    String task_uid,category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_complete_activity);
        //
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);

        task_uid=getIntent().getStringExtra("task_uid");
        category=getIntent().getStringExtra("category");
    }
}