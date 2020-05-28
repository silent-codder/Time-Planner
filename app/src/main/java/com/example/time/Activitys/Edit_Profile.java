package com.example.time.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.time.Fragments.ProfileFragment;
import com.example.time.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Edit_Profile extends AppCompatActivity {

    Button add_img, update_profile;
    EditText phone;
    ImageView img_view;

    StorageReference reference;

    ProgressDialog progressDialog;

    public Uri imguri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        add_img = findViewById(R.id.btn_add_img);
        update_profile = findViewById(R.id.btn_update_profile);

        img_view = findViewById(R.id.img_view);

        reference = FirebaseStorage.getInstance().getReference("Image");

        add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(Edit_Profile.this);
                progressDialog.setMessage("Profile Uploaded....");
                progressDialog.show();

                FileUploder();
            }
        });
    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileUploder() {

        StorageReference ref = reference.child(System.currentTimeMillis()+ "." + getExtension(imguri));

        ref.putFile(imguri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                      //  Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        progressDialog.dismiss();
                        Toast.makeText(Edit_Profile.this, "Profile Update Successfully !!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Edit_Profile.this, ProfileFragment.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    private void FileChooser() {

        Intent intent = new Intent();
        intent.setType("image/'");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode==RESULT_OK && data != null && data.getData() != null)
        {
            imguri = data.getData();
        }
    }
}
