package com.leafsnap.Fragments;

import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.leafsnap.Community.posts_list_adapter;
import com.leafsnap.R;
import com.leafsnap.login_activity;
import com.leafsnap.plant_list_adapter;
import com.leafsnap.plant_post_list_adapter;
import com.leafsnap.view_model;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

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

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        view = inflater.inflate(R.layout.fragment_garden, container, false);

        ((LinearLayout)getActivity().findViewById(R.id.toolbar)).setVisibility(View.VISIBLE);
        sharedPreferences = getContext().getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);

        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        swipe_layout = view.findViewById(R.id.swipe_layout);

        swipe_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    swipe_layout.setRefreshing(false);
               // get_post_list();
            }
        });
        //get_feed_post_list();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

//    private void get_feed_post_list(){
//       RecyclerView post_recyclerView = view.findViewById(R.id.post_recycler_view);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
//
//        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(getContext());
//        post_recyclerView.setLayoutManager(mlinearLayoutManager);
//        ArrayList<view_model> dataholder = new ArrayList<>();
//
//        posts_list_adapter wAdapter = new posts_list_adapter(dataholder, getContext());
//
//        CollectionReference collectionReference = db.collection("posts");
//        collectionReference.whereEqualTo("status",true)//.whereEqualTo("guid",post_user_guid)
//                //.orderBy("date_time", Query.Direction.DESCENDING)
//                .get().addOnCompleteListener(task -> {
//                    if (task.isSuccessful()){
//                        for (QueryDocumentSnapshot s : task.getResult()){
//                            Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
////                            db.collection("account").document(s.getString("guid")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
////                                @Override
////                                public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
////                                    if (task2.isSuccessful()) {
////                                        DocumentSnapshot s2 = task2.getResult();
////                                        if (s2.exists()) {
////                                            Boolean  follow=true;
////                                            if (s.getString("guid").equals(guid))
////                                            {
////                                                follow=false;
////                                            }
////                                            view_model obj = new view_model(
////                                                    s.getString("photo"), //1
////                                                    s.getString("caption"),//2
////                                                    s2.getString("userid"), //3
////                                                    s2.getString("photo"), //4
////                                                    //String.valueOf(s.getDouble("plant_streak").intValue()),
////                                                    String.valueOf(follow),//5
////                                                    "",
////                                                    "",
////                                                    "",
////                                                    "",
////                                                    //String.valueOf(dateFormat.format(s.getTimestamp("date_time").toDate())),
////                                                    s.getString("post_uid"));
////
////                                            dataholder.add(obj);
////                                        }
////                                        //post_recyclerView.setAdapter(wAdapter);
////                                    }
////
////                                }
////                            });
//                        }
//
//                        // get_user_details();
//
//                    }
//                });
//    }
}