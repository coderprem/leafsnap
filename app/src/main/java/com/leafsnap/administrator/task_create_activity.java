package com.leafsnap.administrator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.leafsnap.R;
import com.leafsnap.Utils;
import com.leafsnap.login_activity;

import java.util.HashMap;
import java.util.Map;

public class task_create_activity extends AppCompatActivity {
    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    //location variables
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    Spinner dropdown2;
    int points=0;
    String category="Context";
    Button save_btn;
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_create_activity);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);
        dropdown2 = findViewById(R.id.spinner2);

        //create a list of items for the spinner.
        String[] items2 = new String[]{"5", "10", "15", "50"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
//set the spinners adapter to the previously created one.
        dropdown2.setAdapter(adapter2);
        //2
        Spinner dropdown = findViewById(R.id.spinner);
//create a list of items for the spinner.
        String[] items = new String[]{"Context", "Photo", "Quiz"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        LinearLayout quiz_layout = findViewById(R.id.quiz_layout);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             //Toast.makeText(task_activity.this,String.valueOf(i), Toast.LENGTH_SHORT).show();
                if (i==0){
                    category="Context";
                    quiz_layout.setVisibility(View.GONE);
                } else if (i==1) {
                    category="Photo";
                    quiz_layout.setVisibility(View.GONE);
                } else if (i==2) {
                    category="Quiz";
                    quiz_layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        save_btn=findViewById(R.id.save_btn);
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    create_task();

            }
        });



}

void create_task() {
    EditText task_edit = findViewById(R.id.task_edit);
    points =Integer.parseInt(dropdown2.getSelectedItem().toString());
    String task_title = task_edit.getText().toString().substring(0, 1).toUpperCase() + task_edit.getText().toString().substring(1).toLowerCase();
    if (task_title.length() > 0 && points>0) {

        String task_uid = Utils.randomcode(13);
        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("task_uid", task_uid);
        mapdata.put("guid", guid);
        mapdata.put("task",task_title);
        mapdata.put("category",category);
        mapdata.put("point", points);
        mapdata.put("status", true);
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("task");
        CRefedata.document(task_uid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                if (category.equals("Quiz")){
                    create_quiz_task(task_uid);
                }
                else {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

    void create_quiz_task(String task_uid) {
        EditText task_edit = findViewById(R.id.task_edit);
        points =Integer.parseInt(dropdown2.getSelectedItem().toString());
        EditText option1 = findViewById(R.id.option1_edit);
        EditText option2 = findViewById(R.id.option2_edit);
        EditText option3 = findViewById(R.id.option3_edit);
        EditText option4 = findViewById(R.id.option4_edit);
        EditText option5 = findViewById(R.id.option5_edit);

        String task_title = task_edit.getText().toString().substring(0, 1).toUpperCase() + task_edit.getText().toString().substring(1).toLowerCase();
        if (task_title.length() > 0 && points>0) {
            String quiz_uid = Utils.randomcode(6);
            Map<String, Object> mapdata = new HashMap<>();
            mapdata.put("task_uid", task_uid);
            mapdata.put("guid", guid);
            mapdata.put("task",task_title);
            mapdata.put("quiz_uid", quiz_uid);
            mapdata.put("category",category);
            mapdata.put("point", points);
            //
            if (option1.getText().toString().length()>0){
                mapdata.put("option1", option1.getText().toString());
                mapdata.put("option1_checkbox",((CheckBox)findViewById(R.id.option1_check)).isChecked());
            }

            if (option2.getText().toString().length()>0){
                mapdata.put("option2", option2.getText().toString());
                mapdata.put("option2_checkbox",((CheckBox)findViewById(R.id.option2_check)).isChecked());
            }

            if (option3.getText().toString().length()>0){
                mapdata.put("option3", option3.getText().toString());
                mapdata.put("option3_checkbox",((CheckBox)findViewById(R.id.option3_check)).isChecked());
            }

            if (option4.getText().toString().length()>0){
                mapdata.put("option4", option4.getText().toString());
                mapdata.put("option4_checkbox",((CheckBox)findViewById(R.id.option4_check)).isChecked());
            }

            if (option5.getText().toString().length()>0){
                mapdata.put("option5", option5.getText().toString());
                mapdata.put("option5_checkbox",((CheckBox)findViewById(R.id.option5_check)).isChecked());
            }

            mapdata.put("status", true);
            mapdata.put("date_time", FieldValue.serverTimestamp());
            CollectionReference CRefedata = db.collection("task").document(task_uid).collection("quiz");
            CRefedata.document(quiz_uid).set(mapdata).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}