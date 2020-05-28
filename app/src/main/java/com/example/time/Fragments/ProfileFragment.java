package com.example.time.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    TextView profile_name, profile_profession, profile_dob, profile_email, profile_phone;
    Button logout;
    LottieAnimationView edit_profile;

    DatabaseReference ref;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);

        profile_name = view.findViewById(R.id.profile_name);
        profile_profession = view.findViewById(R.id.profile_profession);
        profile_dob = view.findViewById(R.id.profile_dob);
        profile_email = view.findViewById(R.id.profile_email);
        profile_phone = view.findViewById(R.id.profile_phone);

        logout = view.findViewById(R.id.logout);
        edit_profile = view.findViewById(R.id.edit_profile);

        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        ref = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("Name").getValue().toString();
                String profession = dataSnapshot.child("Profession").getValue().toString();
                String email = dataSnapshot.child("Email").getValue().toString();
                String dob = dataSnapshot.child("DOB").getValue().toString();

                profile_name.setText(name);
                profile_profession.setText(profession);
                profile_dob.setText(dob);
                profile_email.setText(email);
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


        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Edit_Profile.class));
            }
        });

        return view;
    }
}
