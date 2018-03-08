package com.example.mihasz.thingstodo;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    ListView listview;
    ArrayList<Thing> arrayList;
    CustomAdapter arrayAdapter;
    Context thisContext = this;
    File db_file;
    FileInputStream fin;
    ObjectInputStream oin;
    FileOutputStream fout;
    android.app.ActionBar actionBar;
    ObjectOutputStream oout;
    ImageButton imageButton;
    CalendarContract calendarContract;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_settings:
                deleteCheckedThings();
                break;
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //------------------------------------TOOLBAR-ETC---------------------------------------------------
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitleTextColor(-1);
        setSupportActionBar(myToolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.toolbarColor));
        }
        //----------------------------------------------------
        listview = (ListView) findViewById(R.id.list);
        arrayList = new ArrayList<Thing>();
        imageButton = (ImageButton) findViewById(R.id.new_thing_button);
        //-----------------------------------Reading ArrayList from file----------------------------------
        db_file = new File(thisContext.getFilesDir(), "db_file");
        try {
            fin = new FileInputStream(db_file);
            oin = new ObjectInputStream(fin);
            arrayList = (ArrayList<Thing>) oin.readObject();
            oin.close();
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            arrayList = new ArrayList<Thing>();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //-----------------------------------Tworzenie arrayAdapter-----------------------------
        arrayAdapter = new CustomAdapter(this, arrayList);
        listview.setAdapter(arrayAdapter);
        listview.setItemsCanFocus(false);

        //Słuchacz listy THings
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Intent intent = new Intent(thisContext, ViewThing.class);
                    intent.putExtra("thing",(Thing) listview.getItemAtPosition(i));
                    startActivity(intent);
            }
        });


        //Edycja Thinga po kliknięciu i przytrzymaniu w niego
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(thisContext, EditThing.class);
                intent.putExtra("thing", arrayAdapter.getItem(i));
                intent.putExtra("position",i);
                startActivityForResult(intent, 1);
                return true;

            }
        });

    }



    // Usunięcie oznaczonych Thingów
    public void deleteCheckedThings () {
        boolean tmp = true;
        for (int i =arrayList.size()-1;i>=0;i--)
            if (arrayList.get(i).getState())
                tmp = false;
        if (tmp)
            return;

            final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Czy usunąć zaznaczone Things ?");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Tak", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface,int j) {
                    for (int i =arrayList.size()-1;i>=0;i--) {
                        if (arrayList.get(i).getState()) {
                            arrayList.remove(i);
                        }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
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

    //Nowy thing , kliknięcie plusa
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void newThing_function(View view) {
        Intent intent = new Intent(this, NewThing.class);
        startActivityForResult(intent, 1);

    }

    //Tworzenie alarmu
    public void createAlarm(Thing thing) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(thing.getRemind_date());
        Intent intent = new Intent(MainActivity.this, Alarm.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("thing", thing);
        intent.putExtra("bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        long za_ile_alarm = thing.getRemind_date().getTime() - Calendar.getInstance().getTimeInMillis();

        //Toast
        Toast.makeText(this,"Przypomnienie za " +thing.getTTT("dni"),Toast.LENGTH_LONG).show();
    }
    //anulowanie alarmu
    public void cancelAlarm (Thing thing) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        PendingIntent pi = thing.getPendingIntent();
        if (pi != null) {
            am.cancel(pi);
        }
    }

    //Zapisanie listy do pliku, w przypadku zamknięcia aplikacji
    protected void onStop() {
        super.onStop();
        try {
            fout = new FileOutputStream(db_file);
            oout = new ObjectOutputStream(fout);
            oout.writeObject(arrayList);
            oout.close();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




// Po zamknięciu NewThing.act lub EditThing.act
        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra("isnew",true)==false) {
                //jeśli edytowany Thing usuwam alarm i usuwam Thing by go zaraz stworzyć
                    int position = data.getIntExtra("position", -1);
                    Thing tmp = arrayAdapter.getItem(position);
                    if(tmp.isRemind())
                        cancelAlarm(tmp);
                    arrayAdapter.remove(arrayAdapter.getItem(position));
                    arrayAdapter.notifyDataSetChanged();
                }


                Thing t = (Thing) data.getSerializableExtra("thing");
                if (t.isRemind()) {
                    if (t.getRemind_date().after(Calendar.getInstance().getTime()))
                        createAlarm(t);
                    else  {

                        t.setRemind(false);
                        Toast.makeText(this,"Brak przypomnienia",Toast.LENGTH_LONG).show();
                    }
                }

                arrayAdapter.add(t);


            }
            if (resultCode == Activity.RESULT_CANCELED)
                ;
        }
    }


}
