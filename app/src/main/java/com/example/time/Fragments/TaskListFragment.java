package com.example.time.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.time.R;
import com.example.time.Task_DB.Task;
import com.example.time.Adapters.TaskAdapter;
import com.example.time.Task_DB.TaskDatabase;

import java.util.List;


public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    TaskAdapter adapter;

    static TaskDatabase taskDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_list, container, false);

        taskDatabase = Room.databaseBuilder( getActivity(),TaskDatabase.class,"db_task").allowMainThreadQueries().build();


        recyclerView = view.findViewById(R.id.recycle_view);
        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(layoutManager);

        List<Task> list = taskDatabase.taskDAO().getTask();

        adapter = new TaskAdapter(list);
        recyclerView.setAdapter(adapter);


        return view;

    }
}
