package com.example.esp_health_monitor;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBquery {
    public static List<model>list=new ArrayList<>();
    public static List<model>show_list=new ArrayList<>();
    public static void loadData(final adapter adapter, final TextView time,final TextView temp,final TextView bp,final TextView bpm,final TextView sugar,final TextView hb,final TextView remark,final Context context){
        DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("hitman");
        Query databaseReference12 =  databaseReference4.child(FirebaseAuth.getInstance().getUid()).child("health").orderByKey();


        databaseReference12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                Date date = new Date();
//                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
//                                String today = simpleDateFormat.format(date);
                list.clear();
                try {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String date = dataSnapshot.getKey().toString();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String time = dataSnapshot1.getKey().toString();
                            decrypt(dataSnapshot1.child("temp").getValue().toString(), dataSnapshot1.child("blood_pressure").getValue().toString(), dataSnapshot1.child("hb").getValue().toString(), dataSnapshot1.child("sugar").getValue().toString(), dataSnapshot1.child("bps").getValue().toString(), date + " " + time, "");
                        }

                    }

                    Collections.reverse(list);
                    adapter.notifyDataSetChanged();
                    time.setText("Last Update : " + list.get(0).getTime());
                    temp.setText(list.get(0).getTemperature());
                    bp.setText(list.get(0).getBlood_pressure());
                    bpm.setText(list.get(0).getHeart_rate());
                    sugar.setText(list.get(0).getSugar());
                    hb.setText(list.get(0).getHeamoglobin());
                    remark.setText(list.get(0).getRemark());
                }catch (Exception e){
                    String error = e.getMessage().toString();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public static void decrypt(String temp, String bp, String bps, String hb, String sugars, String times,String remark){
        ArrayList<String>arrDecrypted=new ArrayList<>();
        int[][]arr=new int[10][20];

        String[] bparr;

        bparr=bp.split("/");
         String temp_color = null,bp_color = null,hb_color = null,sugar_color = null,bpm_color = null;
         String conclusion="";

        int temp_key=Integer.parseInt(temp)%10;
        int sbp_key=Integer.parseInt(bparr[0])%10;
        int dbp_key=Integer.parseInt(bparr[1])%10;
        int bps_key=Integer.parseInt(bps)%10;
        int hb_key=Integer.parseInt(hb)%10;
        int sugar_key=Integer.parseInt(sugars)%10;

        arr[0][0]=temp_key;
        arr[1][0]=sbp_key;
        arr[2][0]=dbp_key;
        arr[3][0]=bps_key;
        arr[4][0]=hb_key;
        arr[5][0]=sugar_key;

        int tempc=Integer.parseInt(temp)/10;
        int sbpc=Integer.parseInt(bparr[0])/10;
        int dbpc=Integer.parseInt(bparr[1])/10;
        int bpsc=Integer.parseInt(bps)/10;
        int hbc=Integer.parseInt(hb)/10;
        int sugarc=Integer.parseInt(sugars)/10;

        int tempcc=tempc%10;
        int sbpcc=sbpc%10;
        int dbpcc=dbpc%10;
        int bpscc=bpsc%10;
        int hbcc=hbc%10;
        int sugarcc=sugarc%10;

        arr[0][1]=tempc/10;
        arr[1][1]=sbpc/10;
        arr[2][1]=dbpc/10;
        arr[3][1]=bpsc/10;
        arr[4][1]=hbc/10;
        arr[5][1]=sugarc/10;

        if(tempcc==1){
            remark +="Abnormal Temperature | ";
            temp_color="#FFA000";
            conclusion +="Low Temperature Possibility of Hypothermia, ";
        }else if(tempcc >=2){
            remark +="Abnormal Temperature | ";
            temp_color="#B50404";
            conclusion +="High Temperature Possibility of Fever, ";
        }else {
            remark +="Normal Temperature | ";
            temp_color="#028E08";
        }
        if(sbpcc==1 || dbpcc==1){
            remark +="Abnormal BP | ";
            bp_color="#FFA000";
            conclusion +="High Blood Pressure possibility of Hypertension stage-1, ";
        }else if(sbpcc>=2 || dbpcc>=2){
            remark +="Abnormal BP | ";
            bp_color="#B50404";
            conclusion +="High Blood Pressure possibility of Hypertension stage-1, ";
        }else {
            remark +="Normal BP | ";
            bp_color="#028E08";
        }
        if(bpscc==1){
            remark +="Normal HeartBeats | ";
            bpm_color="#028E08";

        }else if(bpscc>=2){
            remark +="Normal HeartBeats | ";
            bpm_color="#028E08";

        }else {
            remark +="Normal HeartBeats | ";
            bpm_color="#028E08";
        }
        if(sugarcc==1){
            remark +="Abnormal Sugar | ";
            sugar_color="#FFA000";
            conclusion +="Abnormal Sugar Level Possibility of Pre-Diabetise,, ";
        }else if(sugarcc>=2){
            remark +="Critical Sugar | ";
            sugar_color="#B50404";
            conclusion +="Very High Sugar Level Possibility of Diabetise,, ";
        }else {
            remark +="Normal Sugar | ";
            sugar_color="#028E08";
        }

        if(hbcc==1){
            remark +="Normal Hemoglobin | ";
            hb_color="#028E08";

        }else if(hbcc==2){
            remark +="Normal Hemoglobin | ";
            hb_color="#028E08";
        }else if(hbcc==0) {
            remark +="Normal Hemoglobin | ";
            hb_color="#028E08";
        }

        for (int i=0;i<6;i++){
            int key = arr[i][0];
            int value = arr[i][1];



            switch (key) {
                case 0:

                    for (int num = 5; num < 500; num++) {
                        if (num * num - num - 10 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 1:

                    for (int num = 5; num < 500; num++) {
                        if ((num * num - num * 2 - 1) == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 2:

                    for (int num = 0; num < 5000; num++) {
                        if (num * num - num - 1 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 3:

                    for (int num = 0; num < 5000; num++) {
                        if (num * 3 * num - num - 1 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 4:

                    for (int num = 0; num < 5000; num++) {
                        if (100 * num - num - 1 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;

                        }
                    }

                    break;
                case 5:

                    for (int num = 0; num < 5000; num++) {
                        if (num*num-num+86 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;

                        }
                    }

                    break;
                case 6:

                    for (int num = 5; num < 500; num++) {
                        if (num * num - num - 1 + num * 3 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;

                        }
                    }

                    break;
                case 7:

                    for (int num = 5; num < 500; num++) {
                        if (num * num - num - 2 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 8:

                    for (int num = 5; num < 500; num++) {
                        if (num * num - num - 3 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                case 9:

                    for (int num = 5; num < 500; num++) {
                        if (num * num - num - 4 == value) {
                            arrDecrypted.add(String.valueOf(num));
                            break;
                        }
                    }

                    break;
                default:
                    arrDecrypted.add(String.valueOf(value));

            }
        }
        try {
            String Temperature1=(arrDecrypted.get(0));
            String bp1=(arrDecrypted.get(1)+"/"+arrDecrypted.get(2));
            String bpm1=(arrDecrypted.get(3));
            String hb1=(arrDecrypted.get(4));
            String sugar1=(arrDecrypted.get(5));
            String time1=(times);

            list.add(new model(Temperature1,bp1,bpm1,hb1,sugar1,time1,remark,temp_color,bp_color,hb_color,sugar_color,bpm_color,conclusion));

        }catch (Exception e){

        }
    }
}
