package com.example.time.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.time.R;
import com.example.time.Task_DB.Task;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class AddTaskFragment extends Fragment implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener  {

     EditText title;
     EditText description;
   //  EditText date, time;
     Button create_task;

    Calendar now = Calendar.getInstance();
    TimePickerDialog tpd;
    DatePickerDialog dpd;
    // Calendar calendar;

    int day, month, year, hour, minute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_add_task, container, false);


        title = view.findViewById(R.id.txt_title);
        description = view.findViewById(R.id.txt_description);
        create_task = view.findViewById(R.id.create_task);

        Button set_remainder = view.findViewById(R.id.set_remainder);

//           date = view.findViewById(R.id.date);
//        time = view.findViewById(R.id.time);time

        dpd = DatePickerDialog.newInstance(
                AddTaskFragment.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        tpd = TimePickerDialog.newInstance(
                AddTaskFragment.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );


//        calendar = Calendar.getInstance();
//
//        day = calendar.get(Calendar.DAY_OF_MONTH);
//        month = calendar.get(Calendar.MONTH);
//        year = calendar.get(Calendar.YEAR);
//
//        hour = calendar.get(Calendar.HOUR);
//        minute = calendar.get(Calendar.MINUTE);
//
//        month += 1;

//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int day) {
//
//                        month += 1;
//                        date.setText(day + "/" + month + "/" + year);
//                    }
//                },year,month,day);
//                datePickerDialog.show();
//            }
//        });
//
//       time.setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View v) {
//               TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
//                   @Override
//                   public void onTimeSet(TimePicker view, int hour, int min) {
//
//                       time.setText(hour + ":" + min);
//
//                   }
//               },hour,minute,false);
//               timePickerDialog.show();
//           }
//       });

        set_remainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });


        create_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // dpd.show(getFragmentManager(), "Datepickerdialog");

                //   int id = Integer.parseInt(e_id.getText().toString());
                String txt_title = title.getText().toString();
                String txt_description = description.getText().toString();

                Task task = new Task();
                //   task.setId(id);
                task.setTitle(txt_title);
                task.setDescription(txt_description);


                TaskListFragment.taskDatabase.taskDAO().AddTask(task);
                Toast.makeText(getActivity(), "Task Add Successfully!!", Toast.LENGTH_SHORT).show();

                task.setTitle("");
                task.setDescription("");

            }
        });

        return view;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);
        Intent intent = new Intent(getContext(),TaskListFragment.class);
        intent.putExtra("test","I am a String");
        NotifyMe notifyMe = new NotifyMe.Builder(getContext())
                .title(title.getText().toString())
                .content(description.getText().toString())
                .color(255,0,0,255)
                .led_color(255,255,255,255)
                .time(now)
                .addAction(intent,"Reset",false)
                .key("test")
                .addAction(new Intent(),"Cancel",true,false)
                .addAction(intent,"Done")
                .large_icon(R.drawable.ic_task_list)
               // .rrule("FREQ=MINUTELY;INTERVAL=5;COUNT=2")
                .build();

                Toast.makeText(getContext(),"Set Reminder !!",Toast.LENGTH_SHORT).show();
    }
}
