package com.example.esp_health_monitor;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.O;


/**
 * A simple {@link Fragment} subclass.
 */
public class trackFragment extends Fragment {
    private TextView connecting;
    public static RecyclerView recyclerView;
    public static adapter adapter;




    public trackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_track, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new adapter(DBquery.list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        connecting = view.findViewById(R.id.connectingtxt);

        MainActivity.CURRENTFRAGMNET="TRACK";
       if(homeFragment.health_info){
           connecting.setVisibility(View.INVISIBLE);
       }else {
           connecting.setVisibility(View.VISIBLE);
        }

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
     TextView textView = new TextView(getContext());
        TextView textView1 = new TextView(getContext());
        TextView textView2 = new TextView(getContext());
        TextView textView3 = new TextView(getContext());
        TextView textView4 = new TextView(getContext());
        TextView textView5 = new TextView(getContext());
        TextView textView6 = new TextView(getContext());
        DBquery.loadData(adapter,(TextView)textView,textView1,textView2,textView3,textView4,textView5,textView6,getContext());
    }
}
