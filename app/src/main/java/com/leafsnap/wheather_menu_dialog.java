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
    public void showBottomDialog(Context context, String temperature, String description, int humidity ) {

        dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.wheather_menu_dialog_layout);

        TextView temperatureTextView = dialog.findViewById(R.id.temperatureTextView);
        TextView descriptionTextView = dialog.findViewById(R.id.descriptionTextView);
        TextView humidityTextView = dialog.findViewById(R.id.humidityTextView);

        temperatureTextView.setText(temperature);
        descriptionTextView.setText(description);
        humidityTextView.setText(String.valueOf(humidity));

        dialog.show();
    }
}
