package com.example.mihasz.thingstodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;

/**
 * Created by mihasz on 11.10.17.
 */

public class EditThing extends NewThing{
    Intent thisIntent;
    int position ;
    Thing thing;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("ThingToEdit");
        this.thisIntent = getIntent();
        thing = (Thing)thisIntent.getSerializableExtra("thing");
        position = thisIntent.getIntExtra("position",-1);
        name_edit.setText(thing.getName());
        place_edit.setText(thing.getPlace());
        date_but.setText(simpleDateFormat.format(thing.getDate()));
        reminder.setChecked(thing.isRemind());
        date=thing.getDate();
        description_edit.setText(thing.getDescription());
        remind_when.setSelection(thing.getRemind_sel());
        beforeDateInMilis = selectBeforeDate(thing.getRemind_sel());
        date_but.setText(simpleDateFormat.format(date));
        time_but.setText(simpleTimeFormat.format(date));

    }


    public void save_click(View view) {
        thisIntent.putExtra("position", position);
        thisIntent.putExtra("isnew", false);
        super.save_click(view);
    }

}
