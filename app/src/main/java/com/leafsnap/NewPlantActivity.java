package com.leafsnap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafsnap.Fragments.GardenFragment;

import java.util.HashMap;
import java.util.Map;

public class NewPlantActivity extends AppCompatActivity {
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    Button savebtn;
    EditText plant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);
        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);

        address_loc = sharedPreferences.getString(login_activity.address_loc_KEY, null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        savebtn = findViewById(R.id.savebtn);
        plant_name = findViewById(R.id.plant_name);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                create_new_plant();
            }
        });

    }

    void create_new_plant(){

        String reg_uid= Utils.randomcode(16);
        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("reg_uid",reg_uid);
        mapdata.put("guid", guid);
        mapdata.put("photo", String.valueOf("https://leafsnap.ibankang.com/app_images/default_icon.png"));
        mapdata.put("like",0);
        mapdata.put("comment", 0);
        mapdata.put("share",0);
        mapdata.put("plant_name",plant_name.getText().toString());
        mapdata.put("user_img","");
        mapdata.put("username","");
        mapdata.put("dob", FieldValue.serverTimestamp());
        mapdata.put("plant_note","hello, this is my mango tree");
        mapdata.put("common_name","Mango");
        mapdata.put("scientific_name","Mangifera indica");
        mapdata.put("family","Anacardiaceae");
        mapdata.put("genus","Mangifera");
        mapdata.put("plant_loc",address_loc);
        mapdata.put("plant_target",0);
        mapdata.put("plant_streak",0);
        mapdata.put("device", android.os.Build.MODEL);
        mapdata.put("status", true);
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("plants");
        CRefedata.document(reg_uid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NewPlantActivity.this, HomeActivity.class));
            }
        });
    }
}