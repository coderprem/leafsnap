package com.leafsnap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    EditText search_edit;
    TextView result_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = findViewById(R.id.search_recycler_view);
        search_edit = findViewById(R.id.search_edit);
        result_txt = findViewById(R.id.result_txt);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>1){
                    get_plant_search_list();
                }
                else {
                    result_txt.setText("Search Result: ");
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                    if (editable.length()>1){
                        get_plant_search_list();
                    }
            }
        });
        search_edit.requestFocus();

    }

    void get_plant_search_list(){

        result_txt.setText("Searching...");
        String search_keyword = search_edit.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        search_list_adapter wAdapter = new search_list_adapter(dataholder, this);
        String field="plant_name";
        search_keyword =search_keyword.substring(0,1).toUpperCase() + search_keyword.substring(1).toLowerCase();

        CollectionReference collectionReference = (CollectionReference) db.collection("plants");
        collectionReference.whereEqualTo("status",true).whereEqualTo("plant_name",search_keyword)
                //.orderBy("date_time",Query.Direction.DESCENDING)

                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size()>0){
                        result_txt.setText("Search Result: "+task.getResult().size());
                       // Toast.makeText(this, "s:"+task.getResult().size(), Toast.LENGTH_SHORT).show();
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
                        recyclerView.setAdapter(wAdapter);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }
}