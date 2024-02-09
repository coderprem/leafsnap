package com.leafsnap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.leafsnap.Fragments.GardenFragment;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class NewPlantActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;

    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    ImageView camera_btn;
    Button savebtn;
    EditText plant_name;
    private Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);
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
        savebtn = findViewById(R.id.savebtn);
        plant_name = findViewById(R.id.plant_name);
        camera_btn = findViewById(R.id.camera_btn);

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

//        savebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (plant_name !=null && camera_btn !=null) {
//                    create_new_plant();
//                }
//                else{
//                    Toast.makeText(NewPlantActivity.this, "Fill required fields!!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

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
                    String plantNameText = plant_name.getText().toString().trim();
                    if (TextUtils.isEmpty(plantNameText)) {
                        Toast.makeText(NewPlantActivity.this, "Please enter a plant name", Toast.LENGTH_SHORT).show();
                    } else {
                        // Save the captured image and plant details
                        uploadImageAndSavePlant(plantNameText);
                    }
                } else {
                    Toast.makeText(NewPlantActivity.this, "Please capture a plant picture", Toast.LENGTH_SHORT).show();
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
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Method to handle result of image capture
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                capturedImage = (Bitmap) extras.get("data");
                // Display the captured image
                camera_btn.setImageBitmap(capturedImage);
            }
        }
    }
//    void create_new_plant(){
//
//        String reg_uid= Utils.randomcode(16);
//        Map<String, Object> mapdata = new HashMap<>();
//        mapdata.put("reg_uid",reg_uid);
//        mapdata.put("guid", guid);
//        mapdata.put("photo", String.valueOf("https://leafsnap.ibankang.com/app_images/default_icon.png"));
//        mapdata.put("like",0);
//        mapdata.put("comment", 0);
//        mapdata.put("share",0);
//        mapdata.put("plant_name",plant_name.getText().toString());
//        mapdata.put("user_img","");
//        mapdata.put("username","");
//        mapdata.put("dob", FieldValue.serverTimestamp());
//        mapdata.put("plant_note","hello, this is my mango tree");
//        mapdata.put("common_name","Mango");
//        mapdata.put("scientific_name","Mangifera indica");
//        mapdata.put("family","Anacardiaceae");
//        mapdata.put("genus","Mangifera");
//        mapdata.put("plant_loc",address_loc);
//        mapdata.put("plant_city",city);
//        mapdata.put("plant_postalcode",postalCode);
//        mapdata.put("plant_state",state);
//        mapdata.put("plant_country",country);
//        mapdata.put("plant_latitude",latitude);
//        mapdata.put("plant_longitude",longitude);
//        mapdata.put("plant_target",0);
//        mapdata.put("plant_streak",0);
//        mapdata.put("device", android.os.Build.MODEL);
//        mapdata.put("status", true);
//        mapdata.put("date_time", FieldValue.serverTimestamp());
//        CollectionReference CRefedata = db.collection("plants");
//        CRefedata.document(reg_uid).set(mapdata).addOnCompleteListener(task2 -> {
//            if (task2.isSuccessful()) {
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(NewPlantActivity.this, HomeActivity.class));
//            }
//        });
//    }

    // Method to upload image to Firebase Storage and save plant details
    private void uploadImageAndSavePlant(String plantNameText) {

        if (capturedImage == null) {
            Toast.makeText(NewPlantActivity.this, "No image captured", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(NewPlantActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savePlantDetails(String imageUrl, String plant_name) {

        String reg_uid= Utils.randomcode(16);

        Map<String, Object> mapdata = new HashMap<>();
        mapdata.put("reg_uid",reg_uid);
        mapdata.put("guid", guid);
        mapdata.put("photo", String.valueOf(imageUrl));
        mapdata.put("like",0);
        mapdata.put("comment", 0);
        mapdata.put("share",0);
        mapdata.put("plant_name",plant_name);
        mapdata.put("user_img","");
        mapdata.put("username","");
        mapdata.put("dob", FieldValue.serverTimestamp());
        mapdata.put("plant_note","hello, this is my mango tree");
        mapdata.put("common_name","Mango");
        mapdata.put("scientific_name","Mangifera indica");
        mapdata.put("family","Anacardiaceae");
        mapdata.put("genus","Mangifera");
        mapdata.put("plant_loc",address_loc);
        mapdata.put("plant_city",city);
        mapdata.put("plant_postalcode",postalCode);
        mapdata.put("plant_state",state);
        mapdata.put("plant_country",country);
        mapdata.put("plant_latitude",latitude);
        mapdata.put("plant_longitude",longitude);
        mapdata.put("plant_target",0);
        mapdata.put("plant_streak",0);
        mapdata.put("device", android.os.Build.MODEL);
        mapdata.put("status", true);
        mapdata.put("date_time", FieldValue.serverTimestamp());
        CollectionReference CRefedata = db.collection("plants");
        CRefedata.document(reg_uid).set(mapdata).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                // Plant data saved successfully
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewPlantActivity.this,plant_details_activity.class);
                intent.putExtra("reg_uid",reg_uid);
                startActivity(intent);
                onBackPressed();
            }
            else {
                // Failed to save plant data
                Toast.makeText(NewPlantActivity.this, "Failed to save plant details", Toast.LENGTH_SHORT).show();
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