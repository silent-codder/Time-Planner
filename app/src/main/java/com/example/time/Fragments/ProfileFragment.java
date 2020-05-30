package com.example.time.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.time.Activitys.Edit_Profile;
import com.example.time.Activitys.WelcomeActivity;
import com.example.time.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextView profile_name, profile_profession, profile_dob, profile_email, profile_phone;
    Button logout;
    LottieAnimationView edit_profile;

    CircleImageView profile;

    StorageReference storageReference;

    private static final int GALLERY_INTENT = 2;

    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;

    ProgressDialog progressDialog, progressDialog2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profile_name = view.findViewById(R.id.profile_name);
        profile_profession = view.findViewById(R.id.profile_profession);
        profile_dob = view.findViewById(R.id.profile_dob);
        profile_email = view.findViewById(R.id.profile_email);
       // profile_phone = view.findViewById(R.id.profile_phone);
        profile = view.findViewById(R.id.profile_img);

        logout = view.findViewById(R.id.logout);
        edit_profile = view.findViewById(R.id.edit_profile);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        progressDialog = new ProgressDialog(getContext());
        progressDialog2 = new ProgressDialog(getContext());

        storageReference = FirebaseStorage.getInstance().getReference();

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });

        progressDialog.setMessage("wait a moment...");
        progressDialog.show();


        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                String name = dataSnapshot.child("Name").getValue().toString();
                String profession = dataSnapshot.child("Profession").getValue().toString();
                String email = dataSnapshot.child("Email").getValue().toString();
                String dob = dataSnapshot.child("DOB").getValue().toString();
                String profile_img = dataSnapshot.child("Profile Img").getValue().toString();

                profile_name.setText(name);
                profile_profession.setText(profession);
                profile_dob.setText(dob);
                profile_email.setText(email);

                Picasso.get().load(profile_img).into(profile);

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check internet connection !!", Toast.LENGTH_SHORT).show();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),WelcomeActivity.class));
                Toast.makeText(getContext(), "Logout Successfully !!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.setMessage("Profile Updated...");
        progressDialog.show();

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null){

            Uri imgUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getContext(),this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                StorageReference filePath = storageReference.child(user + ".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                    final String getUrl = uri.toString();
                                    ref.child("Profile Img")
                                            .setValue(getUrl);
                                Toast.makeText(getActivity(), "Update Profile !!", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
