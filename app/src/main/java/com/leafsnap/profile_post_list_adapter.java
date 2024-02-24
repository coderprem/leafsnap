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

import com.leafsnap.Community.posts_activity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class profile_post_list_adapter extends RecyclerView.Adapter<profile_post_list_adapter.viewholder> {

    ArrayList<view_model> dataholder;
    Context context;

    public profile_post_list_adapter(ArrayList<view_model> dataholder, Context context){
        this.dataholder = dataholder;
        this.context = context;
    }
    @NonNull
    @Override
    public profile_post_list_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_plant_row, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull profile_post_list_adapter.viewholder holder, int position) {
        final view_model temp = dataholder.get(position);
        if (temp.getText1().length()>10) {
            Picasso.get().load(temp.getText1()).into(holder.plant_img);
        }

        holder.layout_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, posts_activity.class);
                intent.putExtra("guid", temp.getText9());
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

        TextView plant_name_txt, plant_dob_txt, plant_loc_txt, plant_streak_txt;
        ImageView plant_img, plant_streak_img;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            layout_cardview = itemView.findViewById(R.id.layout_cardview);
            plant_img = itemView.findViewById(R.id.plant_img);
        }
    }
}
