package com.robokidz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.robokidz.controller.arrayAdapter.myArrayAdapter;
import com.robokidz.controller.values.Instructions;
import com.robokidz.controller.values.ValueOf;
import com.google.gson.Gson;

public class EditActivity extends AppCompatActivity {
    static Gson gson = new Gson();
    SharedPreferences preferences;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;
    Instructions instructions;

    Context appContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = this;
        setContentView(R.layout.activity_edit);
        preferences = getSharedPreferences(ValueOf.getPrefrence(), MODE_PRIVATE);
        editor = preferences.edit();
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);

        instructions = gson.fromJson(preferences.getString(ValueOf.getInstruction(), gson.toJson(new Instructions())), Instructions.class);
        String jsonString = gson.toJson(instructions);
        String[] pairs = jsonString.split(",");
        String[] keys = new String[pairs.length];
        String[] values = new String[pairs.length];
        for (int i = 0; i < pairs.length; i++) {
            String[] temp = pairs[i].split(":");
            keys[i] = temp[0];
            values[i] = temp[1];
        }
        myArrayAdapter adapter = new myArrayAdapter(this, keys, values);
        ListView listView = findViewById(R.id.edit_listview);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(EditActivity.this, "Clicked : " + adapter.keys[i], Toast.LENGTH_SHORT).show();
                builder = new AlertDialog.Builder(appContext);
                builder.setTitle("Enter new value for " + keys[i].toUpperCase());
                View popview = getLayoutInflater().inflate(R.layout.pop_up_layout, null);
                builder.setView(popview);
                EditText text = popview.findViewById(R.id.custom_keymap_text);
                Button button = popview.findViewById(R.id.collect_keymap_btn);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StringBuilder temp = new StringBuilder("\"\"");
                        temp.insert(1, text.getText().toString());
                        if (i == adapter.keys.length - 1)
                            temp.append("}");
                        if (temp.length() > 2) {
                            adapter.values[i] = temp.toString();
                            adapter.notifyDataSetChanged();
                        }
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });

        Button button = findViewById(R.id.saveKeymap);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < adapter.values.length; i++) {
                    s.append(adapter.keys[i]);
                    s.append(":");
                    s.append(adapter.values[i]);
                    s.append(',');
                }
                s.deleteCharAt(s.length() - 1);
                Log.d("Saving KeyMapping", s.toString());
                editor.putString(ValueOf.getInstruction(), s.toString());
                editor.commit();
                Toast.makeText(appContext, "Values Saved successfully", Toast.LENGTH_LONG).show();
            }
        });

        Button reset = findViewById(R.id.resetKeymap);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(ValueOf.getInstruction(), gson.toJson(new Instructions()));
                editor.commit();
                Toast.makeText(appContext, "Key-Mapping reset successful.", Toast.LENGTH_SHORT).show();
                String jsonString = gson.toJson(new Instructions());
                String[] pairs = jsonString.split(",");
//                String[] keys = new String[pairs.length];
//                String[] values = new String[pairs.length];
                for (int i = 0; i < pairs.length; i++) {
                    String[] temp = pairs[i].split(":");
                    adapter.keys[i] = temp[0];
                    adapter.values[i] = temp[1];
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


}