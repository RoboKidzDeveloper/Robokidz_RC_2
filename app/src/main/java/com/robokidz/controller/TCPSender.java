package com.example.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPSender {
    public void sendTCP(String serverIpAddress, int serverPort, String messageToSend) {
        try {
            // Create a socket to the server
            Socket socket = new Socket(serverIpAddress, serverPort);

            // Get an output stream to send data to the server
            OutputStream outputStream = socket.getOutputStream();

            // Send the message as bytes
            byte[] messageBytes = messageToSend.getBytes();
            outputStream.write(messageBytes);

            // Close the socket and the output stream
            socket.close();
            outputStream.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
