package com.leafsnap;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;

import com.android.volley.RequestQueue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.leafsnap.walkthrough_screen.walkththough_navigation_activity;

import java.util.List;

public class welcome_activity extends AppCompatActivity {
    String setdefault = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null;

    SharedPreferences sharedpreferences;
    //location
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    Animation bottomUp, bottomdown, AnimationIN, AnimationOUT;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        //
        sharedpreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedpreferences.getString(login_activity.Guid_KEY, null);
        account_type = sharedpreferences.getString(login_activity.account_type_key, null);
        loginname = sharedpreferences.getString(login_activity.loginname_KEY, null);
        loginkey = sharedpreferences.getString(login_activity.loginkey_KEY, null);
        timezone = sharedpreferences.getString(login_activity.Timezone_KEY, null);
        address_loc= sharedpreferences.getString(login_activity.address_loc_KEY, null);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Dexter.withContext(this).withPermissions(
                Manifest.permission.INTERNET,
                Manifest.permission.MANAGE_OWN_CALLS,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.FOREGROUND_SERVICE,
                Manifest.permission.WAKE_LOCK,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.MODIFY_AUDIO_SETTINGS,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.USE_FULL_SCREEN_INTENT,
                Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.CAMERA
        ).withListener(new MultiplePermissionsListener()
        {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                loginStart();
            }
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken)
            {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    private void loginStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (guid !=null && user !=null) {
                 Intent intent = new Intent(welcome_activity.this, HomeActivity.class);
                startActivity(intent);
                this.finish();
                // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }
        else
        {
            Intent intent = new Intent(welcome_activity.this, walkththough_navigation_activity.class);
            startActivity(intent);
          this.finish();
        // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

    }
}