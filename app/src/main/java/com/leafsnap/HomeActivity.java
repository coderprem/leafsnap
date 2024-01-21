package com.leafsnap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leafsnap.Fragments.GardenFragment;
import com.leafsnap.Fragments.HomeFragment;
import com.leafsnap.Fragments.MapFragment;
import com.leafsnap.Fragments.ProfileFragment;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    String guid_sip = null, token, business = null, setdefault = null, email_id = null, constr = null, aguid, guid = null, login_photo = null, login_mobile = null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null, device_id = null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    BottomSheetDialog dialog;
    TextView loc_nametxt;
    LocationManager locationManager;

    //Toolbar toolbar;


    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY, null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY, null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY, null);
        address_loc = sharedPreferences.getString(login_activity.login_photo_KEY, null);

//locationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Location
        LocationListener locationListener = new MyLocationListener();


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListener);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);

        //Search BTN
        ImageView searchButton = findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hideViewsForSearch();
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                //openSearchActivity();
            }
        });

        //Menu BTN
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
                               startActivity(new Intent(HomeActivity.this,profile_activity.class));
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
       //bottom menu


        replaceFragment(new HomeFragment());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomDialog(HomeActivity.this);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    replaceFragment(new HomeFragment());
                } else if (item.getItemId() == R.id.garden) {
                    replaceFragment(new GardenFragment());
                } else if (item.getItemId() == R.id.map) {
                    replaceFragment(new MapFragment());
                } else if (item.getItemId() == R.id.profile) {
                    replaceFragment(new ProfileFragment());
                }
                return true;
            }
        });

    }

    //
    private  void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog(Context context) {

        dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout newplantLayout = dialog.findViewById(R.id.layoutNewPlant);
        LinearLayout streakLayout = dialog.findViewById(R.id.layoutStreak);
        LinearLayout exploreLayout = dialog.findViewById(R.id.layoutExplore);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        newplantLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                startActivity(new Intent(HomeActivity.this,NewPlantActivity.class));
                //Toast.makeText(HomeActivity.this,"Upload Pic of new Plant",Toast.LENGTH_SHORT).show();

            }
        });

        streakLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomeActivity.this,"Click plant pic to maintain your Streak",Toast.LENGTH_SHORT).show();

            }
        });

        exploreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(HomeActivity.this,"Click plant pic to get info..",Toast.LENGTH_SHORT).show();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

    dialog.show();

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
        finish();
    }

    private void hideViewsForSearch() {
        //toolbar.setVisibility(View.GONE);
        bottomNavigationView.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }

    private void showViewsAfterSearch() {
        //toolbar.setVisibility(View.VISIBLE);
        bottomNavigationView.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
    }

//    private void openSearchActivity() {
//        // Replace SearchFragment with your actual fragment class.
//        Fragment searchFragment = new SearchFragment();
//
//        // Begin the transaction
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        // Replace the current fragment with the new one
//        transaction.replace(R.id.frame_layout, searchFragment);
//
//        // Add the transaction to the back stack
//        transaction.addToBackStack(null);
//
//        // Commit the transaction
//        transaction.commit();
//    }


    public void onBackPressed() {
        // Handle the back press to show the views after exiting the SearchFragment
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            showViewsAfterSearch();
        } else {
            super.onBackPressed();
        }
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {

            //Log.v(TAG, latitude);
            latitude = String.valueOf(loc.getLatitude());
            longitude = String.valueOf(loc.getLongitude());

            /*------- To get city name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                String speed = "0";
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses != null && addresses.size() > 0) {
                 /*   String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();*/

                    loc_name = addresses.get(0).getFeatureName();
                    address_loc = addresses.get(0).getAddressLine(0);
                    city = addresses.get(0).getLocality();
                    postalCode = addresses.get(0).getPostalCode();
                    state = addresses.get(0).getAdminArea();
                    country = addresses.get(0).getCountryName();
                    // code = addresses.get(0).getCountryCode();
                    //loc_nametxt.setText(String.valueOf(address_loc));
                    //
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(login_activity.latitude_key, latitude);
                    editor.putString(login_activity.longitude_key, longitude);
                    editor.putString(login_activity.address_loc_KEY, address_loc);
                    editor.putString(login_activity.loc_name_key, loc_name);
                    editor.putString(login_activity.city_key, city);
                    editor.putString(login_activity.postalCode_key, postalCode);
                    editor.putString(login_activity.state_key, state);
                    editor.putString(login_activity.country_key, country);
                    editor.putString(login_activity.letlong_KEY, state);
                    editor.putString(login_activity.country_key, country);
                    //editor.putString(login_activity.code_key,code);
                    //editor.putString(login_activity.flag_key,flag);
                    // editor.putString(login_activity.ip_key, Inet4Address.getLocalHost().getHostAddress());

                    editor.apply();
                    //

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
