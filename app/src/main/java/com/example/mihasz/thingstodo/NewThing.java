package com.example.mihasz.thingstodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewThing extends AppCompatActivity {
    Button save;
    EditText name_edit;
    EditText place_edit;
    EditText description_edit;
    Button date_but;
    int remind_sel;
    Button time_but;
    Spinner remind_when;

    CheckBox reminder;
    Date date;
    long beforeDateInMilis;
    Context thiscontext =this;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("kk:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_thing);

        //------------------------------------TOOLBAR-----------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(-1);
        setSupportActionBar(myToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.toolbarColor));
        }
        //----------------------------------------------------
        setTitle("ThingToCreate");
        save = (Button) findViewById(R.id.button);
        name_edit = (EditText) findViewById(R.id.name_edit);
        place_edit = (EditText) findViewById(R.id.place_edit);
        description_edit = (EditText) findViewById(R.id.description_edit);
        remind_when = (Spinner) findViewById(R.id.remind_when);
        date = Calendar.getInstance().getTime();
        reminder = (CheckBox) findViewById(R.id.reminder);
        date_but = (Button) findViewById(R.id.date_but);
        time_but = (Button) findViewById(R.id.time_but);
        date_but.setText(simpleDateFormat.format(date));
        time_but.setText(simpleTimeFormat.format(date));


        // Spinner
        remind_sel = 0;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.when_remind_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        remind_when.setAdapter(adapter);
        remind_when.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                beforeDateInMilis = selectBeforeDate(i);
                remind_sel = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                beforeDateInMilis = 0;

            }
        });



        time_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(thiscontext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        date.setHours(i);
                        date.setMinutes(i1);
                        time_but.setText(simpleTimeFormat.format(date));
                    }
                },getTime(date,1),getTime(date,2),true);
                timePickerDialog.show();
            }
        });


        date_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(thiscontext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int yearpick, int monthpick, int daypick) {
                        date = new Date(yearpick-1900,monthpick,daypick);
                        date_but.setText(simpleDateFormat.format(date));
                        date.setHours(Calendar.getInstance().getTime().getHours());
                        date.setMinutes(Calendar.getInstance().getTime().getMinutes());

                    }
                },getDate(date,3),getDate(date,2)-1,getDate(date,1) );
                datePickerDialog.show();

            }
        });

    }

    public long selectBeforeDate(int n) {
        switch (n) {
            case 0:
                return 0;
            case 1:
                return 60*60*1000;
            case 2:
                return 2* 60 * 60 *1000;
            case 3:
                return 24*60*60*1000;
            case 4:
                return 2*24*60*60*1000;
            case 5:
                return 7*24*60*60*1000;
            case 6:
                return 14*24*60*60*1000;
        }
        return 0;

    }

    int getTime(Date date , int pos) {
        String dateString = simpleTimeFormat.format(date);
        switch (pos) {
            case 1:
                return Integer.parseInt(dateString.substring(0,2));

            case 2:
                return Integer.parseInt((dateString.substring(3,5)));

        }
        return -1;

    }

    int getDate(Date date, int pos) {
        String dateString = simpleDateFormat.format(date);
        switch (pos) {
            case 1:
                return Integer.parseInt(dateString.substring(0,2));

            case 2:
                return Integer.parseInt((dateString.substring(3,5)));

            case 3:
                return Integer.parseInt(dateString.substring(6,10));
        }
        return -1;

    }

    public void save_click(View view) {
        String place_tmp =null ;
        Intent thisIntent = getIntent();
        if (place_edit.getText().length()>0)
            place_tmp = place_edit.getText().toString();
        Date remind_date = new Date(date.getTime()-beforeDateInMilis);
        if (!reminder.isChecked())
            remind_date = null;

        thisIntent.putExtra("thing",new Thing(name_edit.getText().toString(),date,remind_date,place_tmp,description_edit.getText().toString(),remind_sel));

        if ((name_edit.getText().length()>0)&&(date_but.getText().length()>0)) {

            setResult(Activity.RESULT_OK, thisIntent);
        }
        else
            setResult(Activity.RESULT_CANCELED,thisIntent);
        finish();
    }

    /**
     * Created by mihasz on 07.10.17.
     */
}
