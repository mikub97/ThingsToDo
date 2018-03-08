package com.example.mihasz.thingstodo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by mihasz on 11.10.17.
 */
class CustomAdapter extends ArrayAdapter <Thing>  {

    ArrayList arrayList ;
    public CustomAdapter(Context context, ArrayList<Thing> arrayList) {
        super(context, 0, arrayList);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        this.arrayList = arrayList;


    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.from(getContext()).inflate(R.layout.thing_layout, viewGroup, false);
        }
        ImageView remind_icon = (ImageView) view.findViewById(R.id.alarm_image);
        TextView textView_name = (TextView)view.findViewById(R.id.name_tv);
        TextView textView_date = (TextView)view.findViewById(R.id.date_tv);
        CheckBox box = (CheckBox) view.findViewById(R.id.done_checkbox);
        box.setChecked(((Thing) arrayList.get(i)).getState());
        if(!((Thing) arrayList.get(i)).isRemind()) {
            remind_icon.setVisibility(View.INVISIBLE);
        }
        else
            remind_icon.setVisibility(View.VISIBLE);
        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                getItem(i).setState(b);
            }
        });
        textView_name.setText(this.getItem(i).getName());
        textView_date.setText(new SimpleDateFormat("dd.MM.yyyy").format(this.getItem(i).getDate()));
        insertionSort(arrayList);
        notifyDataSetChanged();
        return  view;

    }

    void  insertionSort( ArrayList <Thing> list) {
        Thing temp;
        int i, j;
        for (i = 1; i < list.size(); i++) {
            temp = list.get(i);
            for (j = i - 1; j >= 0 && list.get(j).compareTo(temp) > 0; j--) {
                list.set(j + 1,list.get(j));
            }
            list.set(j + 1, temp);

        }


    }
}


