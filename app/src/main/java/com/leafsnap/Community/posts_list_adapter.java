package com.leafsnap.Community;

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

import com.leafsnap.HomeActivity;
import com.leafsnap.R;
import com.leafsnap.plant_details_activity;
import com.leafsnap.view_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class posts_list_adapter extends RecyclerView.Adapter<posts_list_adapter.viewholder> {

    ArrayList<view_model> dataholder;
    Context context;

    public posts_list_adapter(ArrayList<view_model> dataholder, Context context){
        this.dataholder = dataholder;
        this.context = context;
    }
    @NonNull
    @Override
    public posts_list_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull posts_list_adapter.viewholder holder, int position) {
        final view_model temp = dataholder.get(position);
        holder.post_caption_txt.setText(temp.getText2());
      holder.username_txt.setText(temp.getText3());
        holder.post_date_time_txt.setText(temp.getText9());
       // Picasso.get().load(temp.getText1()).into(holder.post_img);
        if (temp.getText1().length()>10) {
            Picasso.get().load(temp.getText1()).into(holder.post_img);
        }
        if (temp.getText4().length()>10) {
            Picasso.get().load(temp.getText4()).into(holder.user_img);
        }

        if (temp.getText5().equals("true")){
            holder.follow_txt.setVisibility(View.VISIBLE);
        }
        else {
            holder.follow_txt.setVisibility(View.GONE);
        }
        holder.layout_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HomeActivity.class);
                intent.putExtra("post_uid", temp.getText10());
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

        TextView post_caption_txt, username_txt, post_date_time_txt, follow_txt;
        ImageView post_img, user_img;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            layout_cardview = itemView.findViewById(R.id.layout_cardview);
            post_img = itemView.findViewById(R.id.post_img);
            post_caption_txt = itemView.findViewById(R.id.post_caption_txt);

           user_img = itemView.findViewById(R.id.user_photo_img);
            username_txt= itemView.findViewById(R.id.username_txt);

            follow_txt=itemView.findViewById(R.id.follow_txt);
//
            post_date_time_txt = itemView.findViewById(R.id.post_date_time_txt);
        }
    }
}
