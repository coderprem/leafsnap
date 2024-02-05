package com.leafsnap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafsnap.OkHttpUtilAsync;
import com.leafsnap.R;
import com.leafsnap.home_plant_list_adapter;
import com.leafsnap.login_activity;
import com.leafsnap.plant_list_adapter;
import com.leafsnap.target_weather;
import com.leafsnap.view_model;
import com.leafsnap.weather;
import com.leafsnap.weatherapi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //String url = "https://api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apikey = "b340dc9bd5c7d02984b0e297b52b440f";
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    TextView plant_loc_txt;
    View view;
    SwipeRefreshLayout swipe_layout;
    RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageSlider mainslider;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        ((LinearLayout)getActivity().findViewById(R.id.toolbar)).setVisibility(View.VISIBLE);
        // Inflate the layout for this fragment

        sharedPreferences = getContext().getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);

        latitude = sharedPreferences.getString(login_activity.latitude_key, null);
        longitude = sharedPreferences.getString(login_activity.longitude_key, null);
        loc_name = sharedPreferences.getString(login_activity.loc_name_key, null);
        address_loc = sharedPreferences.getString(login_activity.address_loc_KEY, null);
        city = sharedPreferences.getString(login_activity.city_key, null);
        postalCode = sharedPreferences.getString(login_activity.postalCode_key, null);
        state = sharedPreferences.getString(login_activity.state_key, null);
        country = sharedPreferences.getString(login_activity.country_key, null);
        code = sharedPreferences.getString(login_activity.postalCode_key, null);
        flag = sharedPreferences.getString(login_activity.flag_key, null);
        device = sharedPreferences.getString(login_activity.device_key, null);
        device_id = sharedPreferences.getString(login_activity.device_id_key, null);

        plant_loc_txt = view.findViewById(R.id.plant_loc_txt);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        swipe_layout = view.findViewById(R.id.swipe_layout);
        recyclerView = view.findViewById(R.id.recycler_view);




        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_plant_list();
                slide_paly();
            }
        });

        slide_paly();

        view.findViewById(R.id.temp_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getweather();
            }
        });
        return view;
    }

    public void getweather() {
        TextView temp_txt=view.findViewById(R.id.temp_txt);
        OkHttpUtilAsync okHttpUtilAsync=new OkHttpUtilAsync();
        okHttpUtilAsync.fetchNetwork();
        Toast.makeText(getContext(), "1"+String.valueOf(okHttpUtilAsync.fetchNetwork()), Toast.LENGTH_SHORT).show();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.openweathermap.org/data/2.5/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//      //  Toast.makeText(getContext(), "0"+retrofit.toString(), Toast.LENGTH_SHORT).show();
//        weatherapi myapi = retrofit.create(weatherapi.class);
//        Call<target_weather> targetWeather = myapi.getweather("Delhi",apikey);
//        targetWeather.enqueue(new Callback<target_weather>() {
//            @Override
//            public void onResponse(Call<target_weather> call, Response<target_weather> response) {
//                Toast.makeText(getContext(), "Error 1"+response.body(), Toast.LENGTH_SHORT).show();
//             if (response.code()==404){
//
//              } else if (!(response.isSuccessful())) {
//                 Toast.makeText(getContext(), "2"+response.code(), Toast.LENGTH_SHORT).show();
//                }
////
////                target_weather mydata = response.body();
////                weather weather = mydata.getWeather();
////                Double temp = weather.getTemp();
////                Integer temperature = (int)(temp-273.15);
////                temp_txt.setText(String.valueOf(temperature)+"C");
//            }
//
//            @Override
//            public void onFailure(Call<target_weather> call, Throwable t) {
//                Toast.makeText(getContext(), "3"+t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
    }
    void slide_paly()
    {

        mainslider = (ImageSlider)view.findViewById(R.id.image_slider);
        final List<SlideModel> remoteimages=new ArrayList<>();

        CollectionReference collectionReference = db.collection("slider");
        collectionReference//.whereEqualTo("status",true).whereEqualTo("guid",guid)

                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {

                        for (QueryDocumentSnapshot s : task.getResult()) {

                            remoteimages.add(new SlideModel(s.getString("url"), s.getString("title"), ScaleTypes.FIT));
                        }
                        mainslider.setImageList(remoteimages, ScaleTypes.FIT);
                    }


                });
    }

    @Override
    public void onResume() {
        super.onResume();
        plant_loc_txt.setText(address_loc);
        get_plant_list();
    }

    void get_plant_list(){


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mlinearLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        home_plant_list_adapter wAdapter = new home_plant_list_adapter(dataholder, getContext());

        CollectionReference collectionReference = db.collection("plants");
        collectionReference.whereEqualTo("status",true).whereEqualTo("guid",guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("photo"),
                                    s.getString("plant_name"),
                                    String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),
                                    "Task: Water in evening",
                                    String.valueOf(s.getDouble("plant_streak").intValue()),
                                    "",
                                    "",
                                    "",
                                    "",
                                    s.getString("reg_uid"));

                            dataholder.add(obj);
                        }
                        recyclerView.setAdapter(wAdapter);
                        swipe_layout.setRefreshing(false);
                    }
                });
    }
}