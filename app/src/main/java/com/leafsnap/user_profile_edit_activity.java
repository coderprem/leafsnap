package com.leafsnap;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class user_profile_edit_activity extends AppCompatActivity {

    String guid_sip=null,token,business=null,setdefault=null,email_id = null, constr = null, aguid, guid = null,login_photo =null, login_mobile=null, devicename = null, loginname = null, Login_OTP = null, loginkey = null, timezone = null, session = null, account_type = null,device_id=null;
    SharedPreferences sharedPreferences;
    String latitude = null, longitude = null, address_loc = null, loc_name = null, address = null, city = null, postalCode = null, state = null, country = null, code = null, flag = null, ip = null, device = null;
    FirebaseUser firebaseUser;
    private FirebaseFirestore db;

    ImageView imageView;
    profile_img_edit_dialog imgDialog;
    private StorageReference storageRef;
    private FirebaseApp app;
    private FirebaseStorage storage;
    private Uri selectedImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_edit_activity);
        app = FirebaseApp.getInstance();
        storage =FirebaseStorage.getInstance(app);
        sharedPreferences = getSharedPreferences(login_activity.SHARED_PREFS, Context.MODE_PRIVATE);
        guid = sharedPreferences.getString(login_activity.Guid_KEY,null);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        imageView = findViewById(R.id.user_photo_img);
        imgDialog = new profile_img_edit_dialog();

        findViewById(R.id.edit_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check if an image is selected
                if (selectedImageUri != null) {
                    // Upload the selected image
                    uploadSelectedImage(selectedImageUri);
                } else {
                    // If no image is selected, just save other user details
                    user_edit_save();
                }
            }
        });

        ((ImageView)findViewById(R.id.user_photo_img)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
//                profile_img_edit_dialog imgDialog = new profile_img_edit_dialog();
//                imgDialog.showBottomDialog(user_profile_edit_activity.this,guid);
                imgDialog.showBottomDialog(user_profile_edit_activity.this, guid, imageView);
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        get_user_details();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imgDialog.handleActivityResult(requestCode, resultCode, data);
    }



    void user_edit_save() {
        EditText user_name_txt = findViewById(R.id.user_name_txt);
        EditText user_id_txt = findViewById(R.id.user_id_txt);
        EditText user_bio_txt = findViewById(R.id.user_bio_txt);

        if (user_name_txt.getText().toString().length() < 1) {
            Toast.makeText(this, "Enter Username", Toast.LENGTH_SHORT).show();
        }

        if (user_id_txt.getText().toString().length() < 5) {
            Toast.makeText(this, "Enter Userid", Toast.LENGTH_SHORT).show();
        }

        else {
            Map<String, Object> mapdata = new HashMap<>();
            mapdata.put("name", user_name_txt.getText().toString());
            mapdata.put("userid", user_id_txt.getText().toString());
            mapdata.put("bio", user_bio_txt.getText().toString());
            mapdata.put("update_date_time", FieldValue.serverTimestamp());
            CollectionReference walletcal = db.collection("account");
            walletcal.document(guid).update(mapdata).addOnCompleteListener(task2 -> {
                if (task2.isSuccessful()) {
                   onBackPressed();
                    //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                }
            });
    }
    }
    void get_user_details(){

        DocumentReference docIdRef = db.collection("account").document(guid);
        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot s = task.getResult();
                    if (s.exists()) {
                        if (s.getString("photo")!= null) {
                            ImageView user_img = findViewById(R.id.user_photo_img);
                            Picasso.get().load(s.getString("photo")).into(user_img);
                        }
                        ((TextView)findViewById(R.id.user_name_txt)).setText(s.getString("name"));
                        ((TextView)findViewById(R.id.user_id_txt)).setText(s.getString("userid"));
                        if (s.getString("bio")!="bio") {
                            ((TextView)findViewById(R.id.user_bio_txt)).setText(s.getString("bio"));
                            findViewById(R.id.user_bio_txt).setVisibility(View.VISIBLE);
                        }

                    } else {

                    }
                }
            }
        });

    }

    public void updateProfileImage(Uri imageUri) {
        // Set the selected image URI
        selectedImageUri = imageUri;

        // Display the selected image
        imageView.setImageURI(selectedImageUri);
    }

    private void uploadSelectedImage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference profileImagesRef = storageRef.child("profile_images/" + guid + "/profile.jpg");

        UploadTask uploadTask = profileImagesRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                profileImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        // Update user document in Firestore with the new profile picture URL
                        DocumentReference userRef = db.collection("account").document(guid);
                        userRef.update("photo", imageUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(user_profile_edit_activity.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(user_profile_edit_activity.this, "Failed to update profile image!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(user_profile_edit_activity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    public void updateProfileImage(Uri imageUri) {
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference profileImagesRef = storageRef.child("profile_images/" + guid + "/profile.jpg");
//
//        UploadTask uploadTask = profileImagesRef.putFile(imageUri);
//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                profileImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        String imageUrl = uri.toString();
//                        // Log the URL
//                        Log.d("ProfileImage", "Uploaded image URL: " + imageUrl);
//                        // Update user document in Firestore with the new profile picture URL
//                        DocumentReference userRef = db.collection("account").document(guid);
//                        userRef.update("photo", imageUrl)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            Toast.makeText(user_profile_edit_activity.this, "Profile image updated!", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            Toast.makeText(user_profile_edit_activity.this, "Failed to update profile image!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                Toast.makeText(user_profile_edit_activity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void removeProfileImage() {
//        // Remove profile image from UI
//        imageView.setImageDrawable(null); // or set a placeholder image
//
//        // Remove profile image from Firebase Storage
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReference();
//        StorageReference profileImagesRef = storageRef.child("profile_images/" + guid + "/profile.jpg");
//        profileImagesRef.delete()
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        // Image deleted successfully from Firebase Storage
//                        // Update Firestore document to remove profile image URL
//                        db.collection("account").document(guid)
//                                .update("photo", FieldValue.delete())
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        // Profile image URL removed from Firestore document
//                                        Toast.makeText(user_profile_edit_activity.this, "Profile image removed!", Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        // Failed to remove profile image URL from Firestore document
//                                        Log.e("Firestore", "Error removing profile image URL from Firestore", e);
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Failed to delete image from Firebase Storage
//                        Log.e("FirebaseStorage", "Error deleting image from Firebase Storage", exception);
//                    }
//                });
//    }

}