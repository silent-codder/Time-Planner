package com.example.time.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.room.Room;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.time.Fragments.AddTaskFragment;
import com.example.time.Fragments.ProfileFragment;
import com.example.time.Fragments.ReportFragment;
import com.example.time.Fragments.TaskListFragment;
import com.example.time.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    Fragment selectFragment;

    //static TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // taskDatabase = Room.databaseBuilder( getApplicationContext(),TaskDatabase.class,"DB_task").allowMainThreadQueries().build();



        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.add_task :
                        selectFragment = new AddTaskFragment();
                        break;

                    case R.id.task_list :
                        selectFragment = new TaskListFragment();
                        break;

                    case R.id.report :
                        selectFragment = new ReportFragment();
                        break;

                    case R.id.profile :
                        selectFragment = new ProfileFragment();
                        break;
                }

                if (selectFragment != null)
                {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectFragment).commit();
                }

                return true;
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddTaskFragment()).commit();

    }
}
