package com.leafsnap.administrator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.leafsnap.R;
import com.leafsnap.login_activity;
import com.leafsnap.profile_activity;
import com.squareup.picasso.Picasso;

public class administrator_activity extends AppCompatActivity {
    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    //location variables
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrator_activity);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);


        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView menu_btn =(ImageView) findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }else {
                    ((TextView)drawerLayout.findViewById(R.id.user_email_id_txt)).setText(email_id);
                    ((TextView)drawerLayout.findViewById(R.id.user_name_txt)).setText(loginname);
                    if (login_photo.length()>10) {
                        ImageView user_img = findViewById(R.id.user_photo_img);
                        Picasso.get().load(login_photo).into(user_img);
                    }
                    drawerLayout.openDrawer(GravityCompat.START);
                    NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
                    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                            int id = item.getItemId();
                            if (id == R.id.nav_home){
                                startActivity(new Intent(administrator_activity.this, profile_activity.class));
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }

                            else if (id == R.id.nav_task){
                                startActivity(new Intent(administrator_activity.this, task_list_activity.class));
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }

                            else if (id == R.id.nav_logout){
                                logoutUser();
                                drawerLayout.closeDrawer(GravityCompat.START);
                            }
                            return false;
                        }
                    });
                }

            }
        });
    }

    private void logoutUser() {
        // Log out the user using Firebase Authentication
        FirebaseAuth.getInstance().signOut();

        // Open the login activity
        openLoginActivity();
    }

    // Method to open the login activity
    private void openLoginActivity() {
        Log.d("HomeActivity", "Logging out and opening LoginActivity");
        Intent intent = new Intent(this, login_activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        // finish();
    }
}