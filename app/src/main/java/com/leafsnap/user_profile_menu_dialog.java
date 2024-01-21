package com.leafsnap;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public class user_profile_menu_dialog {
    BottomSheetDialog dialog;
    //Activity mActivity;
    public void showBottomDialog(Context context, String guid) {

        dialog = new BottomSheetDialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.user_profile_menu_dialog_layout);

        LinearLayout layout_setting = dialog.findViewById(R.id.layout_setting);
        LinearLayout layout_notes = dialog.findViewById(R.id.layout_notes);
        LinearLayout layout_logout = dialog.findViewById(R.id.layout_logout);

        layout_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Intent i = new Intent(mActivity, SearchActivity.class);
//                mActivity.startActivity(i);
            }
        });
        dialog.show();
    }
}
