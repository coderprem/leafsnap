package com.leafsnap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leafsnap.administrator.task_edit_activity;

import java.util.ArrayList;

public class task_of_day_adapter extends RecyclerView.Adapter<task_of_day_adapter.viewholder> {

    ArrayList<view_model> dataholder;
    Context context;

    public task_of_day_adapter(ArrayList<view_model> dataholder, Context context){
        this.dataholder = dataholder;
        this.context = context;
    }
    @NonNull
    @Override
    public task_of_day_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_row, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull task_of_day_adapter.viewholder holder, int position) {
        final view_model temp = dataholder.get(position);
        holder.task_name_txt.setText(temp.getText1());
        holder.task_category_txt.setText(temp.getText2());
        holder.task_point_txt.setText(temp.getText3());
        holder.task_date_txt.setText(temp.getText9());

        holder.layout_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, task_edit_activity.class);
                intent.putExtra("category", temp.getText2());
                intent.putExtra("task_uid", temp.getText10());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CardView layout_cardview;

        ImageView task_logo, points_logo;
        TextView task_name_txt, task_category_txt, task_point_txt,task_date_txt;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            layout_cardview = itemView.findViewById(R.id.layout_cardview);
            task_name_txt = itemView.findViewById(R.id.task_name_txt);
            task_category_txt = itemView.findViewById(R.id.task_category_txt);
            task_point_txt = itemView.findViewById(R.id.task_point_txt);
            task_date_txt=itemView.findViewById(R.id.task_date_txt);
//            task_logo = itemView.findViewById(R.id.task_logo_img);
//            points_logo=itemView.findViewById(R.id.points_logo_img);
        }
    }
}
