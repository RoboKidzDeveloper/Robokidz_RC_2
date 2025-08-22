package com.robokidz.controller.arrayAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.robokidz.controller.R;

import java.util.List;
import java.util.zip.Inflater;

public class myArrayAdapter extends ArrayAdapter<String> {

    public String[] keys;
    public String[] values;
    Activity context;

    public myArrayAdapter(@NonNull Activity context, String[] keys, String[] values) {
        super(context, R.layout.activity_edit, keys);
        this.keys = keys;
        this.values = values;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(R.layout.edit_list_item, null, true);
        TextView key = row.findViewById(R.id.edit_item_key);
        TextView value = row.findViewById(R.id.edit_item_value);
        if (position == 0)
            key.setText(keys[position].toUpperCase().replace("{", ""));
        else
            key.setText(keys[position].toUpperCase());
        if (position == keys.length - 1)
            value.setText(values[position].replace("}", ""));
        else
            value.setText(values[position]);
        return row;
    }
}
