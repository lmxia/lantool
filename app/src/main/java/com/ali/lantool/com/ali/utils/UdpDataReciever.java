package com.ali.lantool.com.ali.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ali.lantool.LanDevice;
import com.ali.lantool.MainActivity;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpDataReciever {
    private static String TAG = UdpDataReciever.class.getName();
    private static UdpDataReciever reciever = null;
    private Thread thread = null;
    private static Handler mHandler = null ;

    public static UdpDataReciever getInstance(Handler handler){
        if(reciever == null || handler != mHandler){
            reciever = new UdpDataReciever();
            mHandler = handler;
        }
        return reciever;
    }

    public void stop(){
        if (this.thread != null) {
            this.thread.interrupt();
            this.thread = null;
        }
    }
    public void startup(){
        stop();
        try {
            this.thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        //开始侦听
                        UdpDataReciever.this.serverListener();
                    } catch (Exception eee) {
                        Log.w(UdpDataReciever.TAG, "【IMCORE】本地UDP监听停止了(socket被关闭了?)," + eee.getMessage(), eee);
                    }
                }
            });
            this.thread.start();
        } catch (Exception e) {
            Log.w(TAG, "【IMCORE】本地UDPSocket监听开启时发生异常," + e.getMessage(), e);
        }
    }
    private void serverListener() throws Exception {
        while (true) {
            byte[] receiverBuffer = new byte[150];
            DatagramPacket datagramPacket = new DatagramPacket(receiverBuffer, receiverBuffer.length);
            try {
                DatagramSocket localUDPSocket = UdpSocketProvider.getInstance().getLocalUDPSocket();
                if ((localUDPSocket == null) || (localUDPSocket.isClosed())) {
                    continue;
                }
                localUDPSocket.receive(datagramPacket);
                String[] resvData = new String(datagramPacket.getData()).trim().split(":");
                if (resvData.length != 0 && resvData[0].equals("donkey")) {
                    String name = resvData[1];
                    String serverIp = datagramPacket.getAddress().getHostAddress();
                    sendMsg(makeDevice(serverIp, name));
                }
                else{
                    Log.d("DISCOVERY", "discovered self");
                }
            } catch (InterruptedIOException intr) {
                Log.e("InterruptedIOException", intr.toString());
                break;
            }  catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private LanDevice makeDevice(String serverIp, String hostName) {
        LanDevice device = new LanDevice();
        device.setIp(serverIp);
        device.setHostName(hostName);
        return device;
    }

    private void sendMsg(LanDevice device) {
        Message message = Message.obtain(mHandler);
        message.what = 0;
        message.obj = device;
        mHandler.sendMessage(message);
    }
}
