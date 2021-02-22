package com.example.esp_health_monitor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    private Button continue_btn;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FrameLayout frameLayout;
    private TextView remark_text;
    private View remarkbg;
    private LinearLayout linearLayout_live,home_layout_1;
    private TextView connectingtxt,time;
    public static boolean health_info=false;
    public static List<model> modelList = new ArrayList<>();
    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        frameLayout=getActivity().findViewById(R.id.main_fram_layout);
        remark_text = view.findViewById(R.id.remark_text);
        home_layout_1 = view.findViewById(R.id.home_layout_show);
        progressBar = view.findViewById(R.id.progressBar11);
        continue_btn = view.findViewById(R.id.continue_btn);
        connectingtxt = view.findViewById(R.id.connectingtxt);
        connectingtxt.setVisibility(View.INVISIBLE);



        home_layout_1.setVisibility(View.INVISIBLE);

        remarkbg = view.findViewById(R.id.remark_bg);
        linearLayout_live = view.findViewById(R.id.linearLayout_live);


        final TextView temp1  = view.findViewById(R.id.temp_value);
        final TextView bp1    = view.findViewById(R.id.bp_value);
        final TextView sugar1  = view.findViewById(R.id.sugar_value);
        final TextView hb1      =  view.findViewById(R.id.hb_value);
        final TextView bpm1    = view.findViewById(R.id.heart_rate_value);
        final TextView last_updatetxt    = view.findViewById(R.id.time123);




        firebaseAuth=FirebaseAuth.getInstance();
        continue_btn.setVisibility(View.GONE);
        MainActivity.CURRENTFRAGMNET="HOME";
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFrameLayout(new details_formFragment());
                MainActivity.bottomNavigationView.setVisibility(View.INVISIBLE);
            }
        });

        databaseReference=FirebaseDatabase.getInstance().getReference();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.child("hitman").child(firebaseAuth.getUid()).child("personal").child("wifi").exists()){
                    continue_btn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    home_layout_1.setVisibility(View.INVISIBLE);
                    MainActivity.bottomNavigationView.getMenu().getItem(1).setEnabled(false);
                    MainActivity.bottomNavigationView.getMenu().getItem(2).setEnabled(false);
                }else {

                    continue_btn.setVisibility(View.GONE);
                    progressBar.setVisibility(View.INVISIBLE);
                    String wifi = snapshot.child("hitman").child(firebaseAuth.getUid()).child("personal").child("wifi").getValue().toString();
                    String email = snapshot.child("hitman").child(firebaseAuth.getUid()).child("personal").child("email").getValue().toString();
                    Map<String,Object>users=new HashMap<>();
                    users.put("token", FirebaseInstanceId.getInstance().getToken());
                    users.put("email", email);
                    users.put("uid", FirebaseAuth.getInstance().getUid());
                    databaseReference.child("users").child(wifi).setValue(users);
                    MainActivity.bottomNavigationView.getMenu().getItem(2).setEnabled(true);
                    MainActivity.bottomNavigationView.getMenu().getItem(1).setEnabled(true);
                    if(snapshot.child("hitman").child(firebaseAuth.getUid()).child("health").exists()){
                        home_layout_1.setVisibility(View.VISIBLE);
                        health_info=true;
                        adapter dummy_adapter = new adapter(DBquery.show_list);
                        DBquery.loadData(dummy_adapter,last_updatetxt,temp1,bp1,bpm1,sugar1,hb1,remark_text,getContext());




                    }else {
                        connectingtxt.setVisibility(View.VISIBLE);
                        health_info=false;

                    }



                }
//                String name = snapshot.getValue().toString();
//                Toast.makeText(MainActivity.this, name, Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private void setFrameLayout(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }


}
