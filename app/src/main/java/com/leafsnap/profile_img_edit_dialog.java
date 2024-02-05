package com.leafsnap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;


public class profile_img_edit_dialog {

    private static final int REQUEST_CODE_OPEN_GALLERY = 1;
    BottomSheetDialog dialog;
    String guid;
    ImageView profileImageView;
    user_profile_edit_activity activity;
    Uri selectedImageUri;
    //Activity mActivity;
    public void showBottomDialog(Activity activity, String guid, ImageView profileImageView) {
        this.activity = (user_profile_edit_activity) activity;
        this.guid = guid;
        this.profileImageView = profileImageView;

        dialog = new BottomSheetDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.profile_img_edit_dialog_layout);


        LinearLayout layout_new_profile_img = dialog.findViewById(R.id.layout_new_profile_img);
        LinearLayout layout_remove_profile_img = dialog.findViewById(R.id.layout_remove_profile_img);

        layout_new_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openImageChooser(activity);
            }
        });
        dialog.show();

    }
    private void openImageChooser(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, REQUEST_CODE_OPEN_GALLERY);
    }

    public void handleActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_OPEN_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Update the profile image view
            profileImageView.setImageURI(selectedImageUri);

            if (activity != null) {
                activity.updateProfileImage(selectedImageUri);
            }
            dialog.dismiss();
            Toast.makeText(activity, "Profile image selected!", Toast.LENGTH_SHORT).show();
        }

    }

}
