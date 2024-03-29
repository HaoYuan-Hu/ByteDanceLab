package com.bytedance.practice5.socket;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientSocketThread extends Thread {
    public ClientSocketThread(SocketActivity.SocketCallback callback) {
        this.callback = callback;
    }

    private SocketActivity.SocketCallback callback;
    private Activity activity;
    private boolean stopFlag = false;
    private volatile String message = content;

    //head请求内容
    private static String content = "HEAD /xxjj/index.html HTTP/1.1\r\nHost:www.sjtu.edu.cn\r\n\r\n";
    public synchronized void sendMsg(String msg) {
        this.message = content;
    }

    public void disconnect() {
        stopFlag = true;
    }

    private synchronized void clearMsg() {
        this.message = "";
    }

    @Override
    public void run() {
        // TODO 6 用socket实现简单的HEAD请求（发送content）
        //  将返回结果用callback.onresponse(result)进行展示
        Log.d("socket", "start");
        try {
            Socket socket = new Socket("www.sjtu.edu.cn", 80); //服务器IP及端口
            BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());
            BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
            // 读文件
            double n = 1;
            byte[] data = new byte[1024 * 5];//每次读取的字节数
            int len = -1;
            while (!stopFlag && socket.isConnected()) {
                if (!message.isEmpty()) {
                    Log.d("socket", "客户端发送 " + message);
                    os.write(message.getBytes());
                    os.flush();
                    clearMsg();
                    int receiveLen = is.read(data);
                    if (receiveLen != -1) {
                        String receive = new String(data, 0, receiveLen);
                        Log.d("socket", "客户端收到 " + receive);
                        callback.onResponse(receive);
                    } else {
                        Log.d("socket", "客户端收到-1");
                    }
                }
                sleep(300);
                stopFlag = false;
            }
            Log.d("socket", "客户端断开 ");
            os.flush();
            os.close();
            socket.close(); //关闭socket

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}