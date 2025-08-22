package com.robokidz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.robokidz.controller.values.Instructions;
import com.robokidz.controller.values.ValueOf;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private int port;

    EditText ip_text, port_text;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        builder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);


        //display the logo during 5 seconds,
        new CountDownTimer(2800, 400) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //set the new Content of your activity
                MainActivity.this.setContentView(R.layout.activity_main);
                toolbar = findViewById(R.id.mytoolbar);
                setSupportActionBar(toolbar);
                SharedPreferences preferences = getSharedPreferences(ValueOf.getPrefrence(), MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if (!preferences.contains(ValueOf.getInstruction())) {
                    Gson gson = new Gson();
                    editor.putString(ValueOf.getInstruction(), gson.toJson(new Instructions()));
                    editor.apply();
                }
            }
        }.start();


    }

    public boolean validate(String ip) {
        String[] arr = ip.split("\\.");
        Log.d("MainActivity", "validate: " + ip);
        if (ip.charAt(0) != '0' && arr.length == 4) {
            for (String t : arr) {
                try {
                    int temp = Integer.parseInt(t);
                    if (temp < 0 || temp > 255)
                        return false;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }


    public void test(View view) {
        Log.d("Test", "test: Button is clicked");
//        Toast.makeText(this, "You clicked a button", Toast.LENGTH_SHORT).show();


        ip_text = findViewById(R.id.ip_text);
        port_text = findViewById(R.id.port_text);
//        ip_text.setText("Button testing");
        Intent intent = new Intent(this, ControlActivity.class);
        //read port no.
        String ip = ip_text.getText().toString();
        if (!ip.equals("sssk")) {
            if (ip.equals("")) {
                Toast.makeText(this, "Enter Ip Address", Toast.LENGTH_SHORT).show();
                return;
            }
            //Read IP address
            port = Integer.parseInt(port_text.getText().toString());
            if (port_text.getText().toString().equals("")) {
                Toast.makeText(this, "Enter Port number", Toast.LENGTH_SHORT).show();
                return;
            }

//        Toast.makeText(this, "Testing " + ip + " : " + port, Toast.LENGTH_LONG).show();
            if (!validate(ip)) {
                Toast.makeText(this, "Not a valid IP", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
            if (port <= 0)
                port = 1234;
        }

        intent.putExtra("ip", ip);
        intent.putExtra("port", port);

        startActivity(intent);
    }
}