package com.leafsnap.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;
import com.leafsnap.R;
import com.leafsnap.login_activity;
import com.leafsnap.plant_list_adapter;
import com.leafsnap.profile_menu_dialog;
import com.leafsnap.profile_plant_list_adapter;
import com.leafsnap.profile_post_list_adapter;
import com.leafsnap.user_profile_edit_activity;
import com.leafsnap.view_model;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    SwipeRefreshLayout swipe_layout;
    RecyclerView plant_recyclerView;
    View view;
    private TextView usernameTextView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ((LinearLayout)getActivity().findViewById(R.id.toolbar)).setVisibility(View.GONE);

        sharedPreferences = getContext().getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        swipe_layout = view.findViewById(R.id.swipe_layout);
        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                get_user_details();

            }
        });

        ((ImageView)view.findViewById(R.id.menu_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
                profile_menu_dialog menuDialog = new profile_menu_dialog();
                menuDialog.showBottomDialog(getContext());
            }
        });

        view.findViewById(R.id.profile_edit_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), user_profile_edit_activity.class));
            }
        });

        TabLayout tab=view.findViewById(R.id.profile_tab);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        //get_profile_plant_list();
                        get_profile_post_list();
                        break;

                    case 1:

                        get_saved_plant_list();
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        get_user_details();
    }

    void get_user_details(){

        DocumentReference docIdRef = db.collection("account").document(guid);
        docIdRef.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot s = task.getResult();
                    if (s.exists()) {
                        ImageView user_img=view.findViewById(R.id.user_photo_img);
                        Picasso.get().load(s.getString("photo")).into(user_img);

                        ((TextView)view.findViewById(R.id.profile_username_txt)).setText("@"+ s.getString("userid"));
                        ((TextView)view.findViewById(R.id.user_name_txt)).setText(s.getString("name"));
                        ((TextView)view.findViewById(R.id.user_id_txt)).setText("@"+s.getString("userid"));
                        if (s.getString("bio")!="bio") {
                            ((TextView) view.findViewById(R.id.user_bio_txt)).setText(s.getString("bio"));
                            view.findViewById(R.id.user_bio_txt).setVisibility(View.VISIBLE);
                        }


                        //get plant data
                        //get_profile_plant_list();
                        get_profile_post_list();
                        //get_saved_plant_list();
                        swipe_layout.setRefreshing(false);
                    } else {

                    }
                }
            }
        });

    }

    void get_profile_post_list(){
        plant_recyclerView = view.findViewById(R.id.plant_recyclerView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        plant_recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        profile_post_list_adapter wAdapter = new profile_post_list_adapter(dataholder, getContext());

        CollectionReference collectionReference = db.collection("posts");
        collectionReference.whereEqualTo("status",true).whereEqualTo("guid",guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot s : task.getResult()){

                            view_model obj = new view_model(
                                    s.getString("photo"),
                                    //s.getString("plant_name"),
                                    "",
                                    //String.valueOf(dateFormat.format(s.getTimestamp("dob").toDate())),
                                    "",
                                    //s.getString("plant_loc"),
                                    "",
                                    //String.valueOf(s.getDouble("plant_streak").intValue()),
                                    "",
                                    "",
                                    "",
                                    "",
                                    s.getString(guid),
                                    s.getString("post_uid"));

                            dataholder.add(obj);
                        }
                        plant_recyclerView.setAdapter(wAdapter);
                    }
                });
    }
    void get_profile_plant_list(){
        plant_recyclerView = view.findViewById(R.id.plant_recyclerView);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        plant_recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        profile_plant_list_adapter wAdapter = new profile_plant_list_adapter(dataholder, getContext());

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
                                    s.getString("plant_loc"),
                                    String.valueOf(s.getDouble("plant_streak").intValue()),
                                    "",
                                    "",
                                    "",
                                    "",
                                    s.getString("reg_uid"));

                            dataholder.add(obj);
                        }
                        plant_recyclerView.setAdapter(wAdapter);
                    }
                });
    }

    void get_saved_plant_list(){

        plant_recyclerView = view.findViewById(R.id.plant_recyclerView);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        plant_recyclerView.setLayoutManager(mLayoutManager);
        ArrayList<view_model> dataholder = new ArrayList<>();

        profile_plant_list_adapter wAdapter = new profile_plant_list_adapter(dataholder, getContext());

        CollectionReference collectionReference = db.collection("plants");
        collectionReference.whereEqualTo("status",true).whereNotEqualTo("guid",guid)
                //.orderBy("date_time",Query.Direction.DESCENDING)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
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
                        plant_recyclerView.setAdapter(wAdapter);
                    }
                });
    }

}