package com.leafsnap;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class wheather_menu_dialog {
    BottomSheetDialog dialog;
    //Activity mActivity;
    public void showBottomDialog(Context context, String city, String temperature, double feels_like, int humidity, String wind, String cloud, float pressure, String description ) {

      //  public void showBottomDialog(Context context, String temperature, double feels_like, int humidity, String wind, String cloud, float pressure, String description  ) {
        dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.wheather_menu_dialog_layout);

        TextView cityTextView = dialog.findViewById(R.id.city_txt);
        TextView temperatureTextView = dialog.findViewById(R.id.temp_txt);
        TextView feels_likeTextView = dialog.findViewById(R.id.feels_like_txt);
        TextView humidityTextView = dialog.findViewById(R.id.humidity_txt);
        TextView windTextView = dialog.findViewById(R.id.wind_txt);
        TextView cloudTextView = dialog.findViewById(R.id.cloudiness_txt);
        TextView pressureTextView = dialog.findViewById(R.id.pressure_txt);
        TextView descriptionTextView = dialog.findViewById(R.id.desciption_txt);

        cityTextView.setText(city);
        temperatureTextView.setText(temperature);
        feels_likeTextView.setText(String.valueOf(feels_like));
        humidityTextView.setText(String.valueOf(humidity));
        windTextView.setText(wind);
        cloudTextView.setText(cloud);
        pressureTextView.setText(String.valueOf(pressure));
        descriptionTextView.setText(description);


        dialog.show();
    }
}
