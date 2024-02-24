package com.leafsnap.Community;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.leafsnap.HomeActivity;
import com.leafsnap.R;
import com.leafsnap.Utils;
import com.leafsnap.login_activity;
import com.leafsnap.plant_details_activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class post_create_activity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    ImageView camera_btn;
    Button savebtn;
    EditText post_caption_txt;
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_create_activity);

        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        email_id = sharedPreferences.getString(login_activity.email_id_KEY,null);
        loginname = sharedPreferences.getString(login_activity.loginname_KEY, null);
        login_photo = sharedPreferences.getString(login_activity.login_photo_KEY,null);


        latitude = sharedPreferences.getString(login_activity.latitude_key, null);
        longitude = sharedPreferences.getString(login_activity.longitude_key, null);
        loc_name = sharedPreferences.getString(login_activity.loc_name_key, null);
        address_loc = sharedPreferences.getString(login_activity.address_loc_KEY, null);
        city = sharedPreferences.getString(login_activity.city_key, null);
        postalCode = sharedPreferences.getString(login_activity.postalCode_key, null);
        state = sharedPreferences.getString(login_activity.state_key, null);
        country = sharedPreferences.getString(login_activity.country_key, null);
        code = sharedPreferences.getString(login_activity.postalCode_key, null);
        flag = sharedPreferences.getString(login_activity.flag_key, null);
        device = sharedPreferences.getString(login_activity.device_key, null);
        device_id = sharedPreferences.getString(login_activity.device_id_key, null);


        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        savebtn = findViewById(R.id.save_btn);
        post_caption_txt = findViewById(R.id.caption_txt);
        camera_btn = findViewById(R.id.camera_btn);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        // OnClickListener for camera button
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open camera to capture image
                dispatchTakePictureIntent();
            }
        });

        // OnClickListener for save button
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if plant name is not empty and an image is captured
                if (capturedImage != null) {
                    String plantNameText = post_caption_txt.getText().toString().trim();
                    if (TextUtils.isEmpty(plantNameText)) {
                        Toast.makeText(post_create_activity.this, "Please enter a plant name", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save the captured image and plant details
                        uploadImageAndSavePlant(plantNameText);
                    }
                } else {
                    Toast.makeText(post_create_activity.this, "Please capture a plant picture", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Method to open camera and capture image
    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//        }
        // Create an intent to choose between camera or gallery
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        // Create an intent to capture image from camera
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create chooser intent to display camera or gallery options
        Intent chooserIntent = Intent.createChooser(pickIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {takePictureIntent});

        // Check if there are apps available to handle this intent
        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooserIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Method to handle result of image capture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
//            Bundle extras = data.getExtras();
//            if (extras != null && extras.containsKey("data")) {
//                capturedImage = (Bitmap) extras.get("data");
//                // Display the captured image
//                camera_btn.setImageBitmap(capturedImage);
//            }
//        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                // Image selected from gallery
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    capturedImage = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    // Display the selected image
                    camera_btn.setImageBitmap(capturedImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Image captured from camera
                Bundle extras = data.getExtras();
                if (extras != null && extras.containsKey("data")) {
                    capturedImage = (Bitmap) extras.get("data");
                    // Display the captured image
                    camera_btn.setImageBitmap(capturedImage);
                }
            }
        }
    }

    // Method to upload image to Firebase Storage and save plant details
    private void uploadImageAndSavePlant(String plantNameText) {

        if (capturedImage == null) {
            Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show();
            return;
        }

        // Resize the captured image
        Bitmap resizedImage = resizeBitmap(capturedImage, 500, 500); // Adjust dimensions as needed

        // Convert Bitmap to byte array
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        // Create a unique filename for the image
        String imageName = "plant_image_" + System.currentTimeMillis() + ".jpg";

        // Get reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        // Create reference for the image
        StorageReference imageRef = storageRef.child("plant_images").child(imageName);

        // Upload image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Image uploaded successfully, get the download URL
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    // Save plant details in Firestore
                    savePlantDetails(imageUrl, plantNameText);
                });
            } else {
                // Handle failed image upload
                Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePlantDetails(String imageUrl, String post_caption_txt) {

        String post_uid= Utils.randomcode(16);

        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("post_uid",post_uid);
        mapdata.put("guid", guid);
        mapdata.put("photo", String.valueOf(imageUrl));
        mapdata.put("like",0);
        mapdata.put("comment", 0);
        mapdata.put("share",0);
        mapdata.put("user_img","");
        mapdata.put("username","");
        mapdata.put("caption",post_caption_txt);
        mapdata.put("post_loc",address_loc);
        mapdata.put("post_city",city);
        mapdata.put("post_postalcode",postalCode);
        mapdata.put("post_state",state);
        mapdata.put("post_country",country);
        mapdata.put("post_latitude",latitude);
        mapdata.put("post_longitude",longitude);
        mapdata.put("device", android.os.Build.MODEL);
        mapdata.put("status", true);
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("posts");
        CRefedata.document(post_uid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                // Plant data saved successfully
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(post_create_activity.this, HomeActivity.class);
                intent.putExtra("post_uid",post_uid);
                startActivity(intent);
                onBackPressed();
            }
            else {
                // Failed to save plant data
                Toast.makeText(post_create_activity.this, "Failed to save plant details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to resize a bitmap
    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width > maxWidth || height > maxHeight) {
                float aspectRatio = (float) width / (float) height;

                if (aspectRatio > 1) {
                    width = maxWidth;
                    height = (int) (width / aspectRatio);
                } else {
                    height = maxHeight;
                    width = (int) (height * aspectRatio);
                }
            }

            return Bitmap.createScaledBitmap(bitmap, width, height, true);
        } catch (Exception e) {
            Log.e("NewPlantActivity", "Error resizing bitmap: " + e.getMessage());
            return null;
        }
    }
}
