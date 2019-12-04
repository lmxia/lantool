package com.ali.lantool;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import com.ali.lantool.com.ali.utils.UdpSocketProvider;


public class UdpSenderRunnable implements Runnable {

    private final String mSelf;
    private final String mMask;
    private final Timer mTimer;


    public UdpSenderRunnable(Timer timer, String mIp, String mask) {
        this.mSelf = mIp;
        this.mMask = mask;
        this.mTimer = timer;
    }

    /** Get broadcast address from host and mask */
    public static String getBroadcast(String ip, String mask) {
        String[] ipeek = ip.split("\\.");
        String[] msk = mask.split("\\.");
        String[] brdcst = { "0", "0", "0", "0" };

        for (int i = 0; i < 4; i++) {
            brdcst[i] = String.valueOf(Integer.valueOf(ipeek[i])
                    | (~(Integer.valueOf(msk[i])) & 255));
        }
        return brdcst[0] + "." + brdcst[1] + "." + brdcst[2] + "." + brdcst[3];
    }
    @Override
    public void run() {
        try {
            DatagramSocket mDatagramSocket = UdpSocketProvider.getInstance().getLocalUDPSocket();
            mDatagramSocket.setBroadcast(true);
            String broadcastAddress = getBroadcast(mSelf, mMask);
            InetAddress receiverAddress = InetAddress.getByName(broadcastAddress);
            byte[] data = "are you donkey".getBytes();
            DatagramPacket dataPacket = new DatagramPacket(data,
                    data.length, receiverAddress, 9434);
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        mDatagramSocket.send(dataPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }, 0, 1000 * 30);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}

class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}