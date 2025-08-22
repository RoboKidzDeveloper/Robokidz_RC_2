package com.robokidz.controller;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.robokidz.controller.values.Instructions;
import com.robokidz.controller.values.ValueOf;
import com.robokidz.controller.wifihelper.WifiStatusReceiver;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


public class ControlActivity extends AppCompatActivity {

    ImageView left1;
    ImageView right1;
    ImageView front1;
    ImageView back1;
    Button startbtn;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit_values) {
            editor.putString(ValueOf.getInstruction(), gson.toJson(instructions));
            editor.apply();
            Intent intent = new Intent(applicationContext, EditActivity.class);
            startActivity(intent);
        }
        return true;
    }

    Button stopbtn;
    ImageView left2, right2, front2, back2;

    Boolean iseditable;
    TextView text;
    WifiStatusReceiver statusReceiver;
    static String ip;
    static Integer port;
    static Gson gson = new Gson();
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    ImageView roboimage;
    static ImageView connectionStatus;

    Context applicationContext;


    private Instructions instructions;


    private class sendhelper implements Runnable {
        String ip;
        int port;
        public String data;


        public sendhelper(String ip, int port, String msg) {
            this.ip = ip;
            this.data = msg;
            this.port = port;
        }

        @Override
        public void run() {
            Log.e("transmission", "run: " + data);

            try {
                Socket socket = new Socket(ip, port);

                OutputStream out = socket.getOutputStream();
                out.write(data.getBytes());
                out.flush();
//                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                br.readLine();
//                String s = br.readLine();
                socket.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    sendhelper send_back;
    sendhelper send_check;
    sendhelper send_forward;
    sendhelper send_left;
    sendhelper send_right;
    sendhelper send_triangle, send_cross, send_square, send_stop, send_start, send_circle;


    @Override
    protected void onPause() {
        super.onPause();

        unregisterReceiver(statusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(statusReceiver, intentFilter);

        String ex = preferences.getString(ValueOf.getInstruction(), "Oops");
        Log.e("Test", "Retrieved: " + ex);
        instructions = gson.fromJson(ex, Instructions.class);
        send_back = new sendhelper(ip, port, instructions.getMoveBack());
        send_forward = new sendhelper(ip, port, instructions.getMoveFront());
        send_left = new sendhelper(ip, port, instructions.getMoveLeft());
        send_right = new sendhelper(ip, port, instructions.getMoveRight());
        send_triangle = new sendhelper(ip, port, instructions.getTriangle());
        send_cross = new sendhelper(ip, port, instructions.getCross());
        send_circle = new sendhelper(ip, port, instructions.getCircle());
        send_square = new sendhelper(ip, port, instructions.getSquare());
        send_start = new sendhelper(ip, port, instructions.getStart());
        send_stop = new sendhelper(ip, port, instructions.getStop());

    }

    public static void setStatus(String state) {
        if (state.equals("on")) {
            try {
                SocketAddress sockaddr = new InetSocketAddress(ip, port);
                // Create an unbound socket
                Socket sock = new Socket();

                // This method will block no more than timeoutMs.
                // If the timeout occurs, SocketTimeoutException is thrown.
                int timeoutMs = 1000;   // 1 second
                sock.connect(sockaddr, timeoutMs);
                connectionStatus.setImageResource(R.drawable.connected_ic);

            } catch (IOException e) {
                e.printStackTrace();
                connectionStatus.setImageResource(R.drawable.disconnected_ic);
            }
        } else
            connectionStatus.setImageResource(R.drawable.disconnected_ic);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getSharedPreferences(ValueOf.getPrefrence(), MODE_PRIVATE);
        editor = preferences.edit();

        statusReceiver = new WifiStatusReceiver();
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder request = new NetworkRequest.Builder();
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network);
                }
            }
        };
        instructions = gson.fromJson(preferences.getString(ValueOf.getInstruction(), gson.toJson(new Instructions())), Instructions.class);
        connectivityManager.requestNetwork(request.build(), networkCallback);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_control);
        Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        left1 = findViewById(R.id.mv_left);
        right1 = findViewById(R.id.mv_right);
        front1 = findViewById(R.id.mv_front);
        back1 = findViewById(R.id.mv_back);
        startbtn = findViewById(R.id.start_btn);
        stopbtn = findViewById(R.id.stop_btn);
        left2 = findViewById(R.id.img_left);
        right2 = findViewById(R.id.img_right);
        front2 = findViewById(R.id.img_up);
        back2 = findViewById(R.id.img_down);
//        left2.setImageDrawable(getResources().getDrawable(R.drawable.square, this.getTheme()));
//        right2.setImageDrawable(getResources().getDrawable(R.drawable.circle, this.getTheme()));
//        front2.setImageDrawable(getResources().getDrawable(R.drawable.triangle, this.getTheme()));
//        back2.setImageDrawable(getResources().getDrawable(R.drawable.cross, this.getTheme()));

        connectionStatus = findViewById(R.id.con_status);
        iseditable = false;
        applicationContext = this;

        Intent intent = this.getIntent();
        ip = intent.getExtras().get("ip").toString();
        port = (Integer) intent.getExtras().get("port");
        text = findViewById(R.id.view_ip_text);
        text.setText("Active IP = " + ip + "\nActive Port = " + port);
//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        connManager.setNetworkPreference(1);



        roboimage = findViewById(R.id.imageRobo);
        roboimage.setImageDrawable(getResources().getDrawable(R.drawable.robohead, this.getTheme()));


        roboimage.setForegroundGravity(5);
        roboimage.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        send_back = new sendhelper(ip, port, instructions.getMoveBack());
        send_forward = new sendhelper(ip, port, instructions.getMoveFront());
        send_left = new sendhelper(ip, port, instructions.getMoveLeft());
        send_right = new sendhelper(ip, port, instructions.getMoveRight());
        send_triangle = new sendhelper(ip, port, instructions.getTriangle());
        send_cross = new sendhelper(ip, port, instructions.getCross());
        send_circle = new sendhelper(ip, port, instructions.getCircle());
        send_square = new sendhelper(ip, port, instructions.getSquare());
        send_start = new sendhelper(ip, port, instructions.getStart());
        send_stop = new sendhelper(ip, port, instructions.getStop());


        final Handler handler = new Handler();
        long delay = 23;


        Runnable runcheck = () -> new Thread(send_check).start();

        Runnable runBack = new Runnable() {
            @Override
            public void run() {
                new Thread(send_back).start();
            }
        };

        Runnable runFront = new Runnable() {
            @Override
            public void run() {
                new Thread(send_forward).start();
            }
        };

        Runnable runLeft = new Runnable() {
            @Override
            public void run() {
                new Thread(send_left).start();
            }
        };

        Runnable runRight = new Runnable() {
            @Override
            public void run() {
                new Thread(send_right).start();
            }
        };

        Runnable runTriangle = new Runnable() {
            @Override
            public void run() {
                new Thread((send_triangle)).start();
            }
        };

        Runnable runCross = new Runnable() {
            @Override
            public void run() {
                new Thread(send_cross).start();
            }
        };

        Runnable runSquare = new Runnable() {
            @Override
            public void run() {
                new Thread(send_square).start();
            }
        };

        Runnable runCircle = new Runnable() {
            @Override
            public void run() {
                new Thread(send_circle).start();
            }
        };

        Runnable runStart = new Runnable() {
            @Override
            public void run() {
                new Thread(send_start).start();
            }
        };

        Runnable runStop = new Runnable() {
            @Override
            public void run() {
                new Thread(send_stop).start();
            }
        };

        back1.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (iseditable)
                    return false;
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    // Remove any previously posted runnables (cancel the previous action)
                    handler.removeCallbacks(runBack);

                    // Post a new runnable to be executed after the debounce delay
                    handler.postDelayed(runBack, delay);
                } else {
                    // If the user releases the touch, remove the pending runnable
                    handler.removeCallbacks(runBack);
                }

                return false;
            }
        });

        front1.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (iseditable)
                    return false;
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    // Remove any previously posted runnables (cancel the previous action)
                    handler.removeCallbacks(runFront);

                    // Post a new runnable to be executed after the debounce delay
                    handler.postDelayed(runFront, delay);
                } else {
                    // If the user releases the touch, remove the pending runnable
                    handler.removeCallbacks(runFront);
                }

                return false;
            }
        });

        left1.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (iseditable)
                    return false;
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    // Remove any previously posted runnables (cancel the previous action)
                    handler.removeCallbacks(runLeft);

                    // Post a new runnable to be executed after the debounce delay
                    handler.postDelayed(runLeft, delay);
                } else {
                    // If the user releases the touch, remove the pending runnable
                    handler.removeCallbacks(runLeft);
                }

                return false;
            }
        });

        right1.setOnTouchListener(new View.OnTouchListener() {


            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (iseditable)
                    return false;
                if (motionEvent.getAction() != MotionEvent.ACTION_UP) {
                    // Remove any previously posted runnables (cancel the previous action)
                    handler.removeCallbacks(runRight);

                    // Post a new runnable to be executed after the debounce delay
                    handler.postDelayed(runRight, delay);
                } else {
                    // If the user releases the touch, remove the pending runnable
                    handler.removeCallbacks(runRight);
                }

                return false;
            }
        });

        front2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (iseditable) {
//                    //popup to edit values
//                    builder = new AlertDialog.Builder(applicationContext);
//                    builder.setTitle("Enter new Message to be sent");
//                    View popview = getLayoutInflater().inflate(R.layout.pop_up_layout, null);
//                    builder.setView(popview);
//                    EditText tempText = popview.findViewById(R.id.custom_keymap_text);
//                    Button collect = popview.findViewById(R.id.collect_keymap_btn);
//                    collect.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
////                            instructions.setMoveFront(tempText.getText().toString());
////                            text.append(tempText.getText().toString()+" added.");
//                            String tmp = tempText.getText().toString();
//                            if (tmp != null && tmp.length() > 0) {
//                                instructions.setStart(tmp);
//                                send_triangle = new sendhelper(ip, port, instructions.getStart());
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                    dialog = builder.create();
//                    dialog.show();
//
//                } else
                new Thread(runTriangle).start();
            }
        });

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runCross).start();
            }
        });

        front1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runFront).start();
            }
        });

        right1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runRight).start();
            }
        });

        left1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runLeft).start();
            }
        });

        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runBack).start();
            }
        });


        left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runSquare).start();
            }
        });

        right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runCircle).start();
            }
        });

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(send_start).start();
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(send_stop).start();
            }
        });
    }


}


