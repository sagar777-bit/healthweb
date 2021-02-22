package com.example.esp_health_monitor;

import android.app.Dialog;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>{
    private List<model>modelList;

    public adapter(List<model> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String temp = modelList.get(position).getTemperature();
        String bp = modelList.get(position).getBlood_pressure();
        String bps = modelList.get(position).getHeart_rate();
        String hb = modelList.get(position).getHeamoglobin();
        String sugar = modelList.get(position).getSugar();
        String time = modelList.get(position).getTime();
        String temp_color = modelList.get(position).getTemp_color();
        String bp_color = modelList.get(position).getBp_color();
        String bps_color = modelList.get(position).getBpm_color();
        String hb_color = modelList.get(position).getHb_color();
        String sugar_color = modelList.get(position).getSugar_color();

        holder.setData(temp,bp,bps,hb,sugar,time,temp_color,bp_color,bps_color,hb_color,sugar_color,position);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView temperature,blood_pressure,heamoglobin,heart_rate,sugar,time;
        private Dialog dialog;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            temperature= itemView.findViewById(R.id.recycle_temp);
            blood_pressure= itemView.findViewById(R.id.recycle_bp);
            heamoglobin= itemView.findViewById(R.id.recycle_hb);
            heart_rate= itemView.findViewById(R.id.recycle_bps);
            sugar= itemView.findViewById(R.id.recycle_sugar);
            time= itemView.findViewById(R.id.recycle_time);
        }

        private void setData(String temp, String bp, String bps, String hb, String sugars, String times, String temp_color, String bp_color, String bps_color, String hb_color, String sugar_color, final int position){



                temperature.setText("Temperature : "+temp);
                blood_pressure.setText("BP : "+bp);
                heart_rate.setText("Heart Rate : "+bps);
                heamoglobin.setText("Hemoglobin : "+hb);
                sugar.setText("Sugar : "+sugars);
                time.setText(times);


                temperature.setTextColor(Color.parseColor(temp_color));
            blood_pressure.setTextColor(Color.parseColor(bp_color));
            heart_rate.setTextColor(Color.parseColor(bps_color));
            sugar.setTextColor(Color.parseColor(sugar_color));
            heamoglobin.setTextColor(Color.parseColor(hb_color));

            dialog = new Dialog(itemView.getContext());
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.conclusion_dialog);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            final TextView symptoms = dialog.findViewById(R.id.symptoms);
            final TextView conclusion = dialog.findViewById(R.id.conclusion);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    symptoms.setText(modelList.get(position).getRemark());
                    conclusion.setText(modelList.get(position).getConclusion());
                    dialog.show();
                }
            });
        }
//         String decrypt(int key, int x,int resul) {
//
//
//
//        }


    }
}
