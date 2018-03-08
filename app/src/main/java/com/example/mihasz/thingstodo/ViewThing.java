package com.example.mihasz.thingstodo;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by mihasz on 16.11.17.
 */

public class ViewThing extends AppCompatActivity {
    Intent thisIntent ;
    TextView name_tv;
    TextView place_tv;
    TextView date_tv;
    TableLayout tableLayout;
    TextView description_tv;
    TextView state_tv;
    TextView time_tv;
    TextView remind_tv;
    Thing thing;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("kk:mm");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_thing);
        tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        thisIntent = getIntent();
        name_tv = (TextView) findViewById(R.id.name_tv2);
        place_tv = (TextView) findViewById(R.id.place_tv2);
        date_tv = (TextView) findViewById(R.id.date_tv2);
        state_tv = (TextView)findViewById(R.id.state_tv2);
        remind_tv = (TextView)findViewById(R.id.remind_before);

        thing = (Thing) thisIntent.getSerializableExtra("thing");
        if (thing.getState())
            state_tv.setText("Tak");
        else
            state_tv.setText("Nie");
        time_tv = (TextView) findViewById(R.id.time_tv2);
        description_tv = (TextView) findViewById(R.id.description_tv2);
        name_tv.setText(thing.getName());
        if((thing.getPlace()==null)||(thing.getPlace().length()<1))
            place_tv.setText("-------");
        else
            place_tv.setText(thing.getPlace());
        date_tv.setText(simpleDateFormat.format(thing.getDate()));
        time_tv.setText(simpleTimeFormat.format(thing.getDate()));
        if ((thing.getDescription() == null ) || (thing.getDescription().length()<1))
            description_tv.setText("-------");
        else
            description_tv.setText(thing.getDescription());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.when_remind_array, android.R.layout.simple_spinner_item);
        if (thing.isRemind())
            remind_tv.setText(adapter.getItem(thing.getRemind_sel()));
        else
            remind_tv.setText("Nie");
        findViewById(R.id.place_row_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(ViewThing.this).create();
                alertDialog.setMessage("Czy przejść do aplikacji GoogleMaps w celu znalezienia " +place_tv.getText()+"?");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int j) {
                        String query = new String();
                        query+="geo:0,0?q="+place_tv.getText();
                        Uri gmmIntentUri = Uri.parse(query);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Anuluj",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface,int j) {
                        alertDialog.cancel();;

                    }
                });
                alertDialog.show();
            }
        });


        //------------------------------------TOOLBAR-----------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(myToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.toolbarColor));
        }
        //----------------------------------------------------
        setTitle("ThingToShow");
    }
}

