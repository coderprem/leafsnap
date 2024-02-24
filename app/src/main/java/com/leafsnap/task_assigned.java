package com.leafsnap;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class task_assigned {
    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    //location variables
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    SwipeRefreshLayout swipe_layout;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;
    public void task_assigned(Context context)
    {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = context.getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);

        db.collection("task_assigned").document(guid).collection("list")
                .whereEqualTo("status",false).whereLessThan("timestamp", new Date()).whereGreaterThan("timestamp", new Date()).limit(stop)
                .orderBy("date_time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                       // get_task_list();
                    }else {
                        to_task_assigned();
                    }
                });

    }
    int tasks=0,stop=2;
    void to_task_assigned()
    {
        db.collection("task")
                .whereEqualTo("status",true)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        for (QueryDocumentSnapshot s : task.getResult()) {
                            //find
                            db.collection("task_assigned").document(guid).collection("list")
                                    .whereEqualTo("task_uid",s.getString("task_uid") ).whereGreaterThanOrEqualTo("date_time",new Date())
                                    .get().addOnCompleteListener(task2 -> {
                                        if (task2.isSuccessful() && task2.getResult().size() >0) {
                                            //allrdy
                                        }else {
                                            if (tasks<stop) {
                                                tasks++;
                                                add_task(s.getString("task_uid"));
                                            }else {
                                                //get_task_list();
                                            }
                                        }
                                    });
                            if (tasks==stop) {

                                break;
                            }
                        }
                    }
                });

    }
    void  add_task(String task_uid)
    {
        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("task_uid", task_uid);
        mapdata.put("guid", guid);
        mapdata.put("status", false);
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("task_assigned").document(guid).collection("list");
        CRefedata.document(task_uid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {

            }
        });
    }
}
