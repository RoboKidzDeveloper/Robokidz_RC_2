//package com.example.myapplication;
//
//import android.util.Log;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
//class TCPClient {
//    private static String SERVERIP;
//    private static int SERVERPORT;
//    private Boolean cancelConnection = false;
//    private OnMessageReceived mMessageListener = null;
//    private boolean mRun = false;
//    private OutputStream out;
//    Socket socket;
//
//    interface OnMessageReceived {
//        void messageReceived(String str);
//    }
//
//    TCPClient(OnMessageReceived onMessageReceived, String str, int i) {
//        this.mMessageListener = onMessageReceived;
//        SERVERIP = str;
//        SERVERPORT = i;
//    }
//
//    /* access modifiers changed from: package-private */
//    public void sendMessage(byte[] bArr) {
//        try {
//            this.out.write(bArr);
//        } catch (IOException e) {
//            Log.e("TAG", "Exception during write", e);
//        }
//    }
//
//    /* access modifiers changed from: package-private */
//    public void stopClient() {
//        this.mRun = false;
//        this.cancelConnection = true;
//    }
//
//    /* access modifiers changed from: package-private */
//    public void run() {
//        int i = 1;
//        this.mRun = true;
//        try {
//            InetAddress byName = InetAddress.getByName(SERVERIP);
//            while (true) {
//                if (i <= 5) {
//                    Log.e("TCP Client", "C: Connecting..." + i);
//                    this.socket = new Socket();
//                    this.socket.connect(new InetSocketAddress(byName, SERVERPORT), 2000);
//                    if (this.mMessageListener != null) {
//                        this.mMessageListener.messageReceived("$Conn$");
//                    }
//                }
//            }
//            try {
//                this.out = this.socket.getOutputStream();
//                Log.e("TCP Client", "C: Sent.");
//                Log.e("TCP Client", "C: Done.");
//                InputStream inputStream = this.socket.getInputStream();
//                byte[] bArr = new byte[512];
//                while (this.mRun) {
//                    int read = inputStream.read(bArr);
//                    Log.d("TCP Client", read + "");
//                    int[] iArr = new int[read];
//                    for (int i2 = 0; i2 < read; i2++) {
//                        iArr[i2] = bArr[i2] & 255;
//                    }
//                    String str = new String(iArr, 0, read);
//                    if (this.mMessageListener != null) {
//                        this.mMessageListener.messageReceived(str);
//                    }
//                }
//                try {
//                    this.socket.close();
//                    return;
//                } catch (IOException e) {
//                    e = e;
//                }
////                e.printStackTrace();
//            } catch (Exception e2) {
//                Log.e("TCP", "S: Error", e2);
//                if (this.mMessageListener != null) {
//                    this.mMessageListener.messageReceived("$dis$");
//                }
//                try {
//                    this.socket.close();
//                } catch (IOException e3) {
////                    e = e3;
//                }
//            }
//        } catch (Exception unused) {
//            Log.d("Try", "attempt " + i + " failed");
//            if (this.cancelConnection.booleanValue()) {
//                throw new Exception();
//            } else if (i != 5) {
//                i++;
//            } else {
//                throw new Exception();
//            }
//        } catch (Exception e4) {
//            if (this.cancelConnection.booleanValue()) {
//                Log.d("Cancel", "Connection");
//                return;
//            }
//            OnMessageReceived onMessageReceived = this.mMessageListener;
//            if (onMessageReceived != null) {
//                onMessageReceived.messageReceived("$fail$");
//            }
//            Log.e("TCP", "C: Error", e4);
//        } catch (Throwable th) {
//            try {
//                this.socket.close();
//            } catch (IOException e5) {
//                e5.printStackTrace();
//            }
//            throw th;
//        }
//    }
//}
