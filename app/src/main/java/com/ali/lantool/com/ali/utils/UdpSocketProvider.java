package com.ali.lantool.com.ali.utils;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSocketProvider {
    private static final String TAG = UdpSocketProvider.class.getName();
    private DatagramSocket socket = null;
    private static UdpSocketProvider udpStocketProvider = null;

    public static UdpSocketProvider getInstance(){
        if(udpStocketProvider == null) udpStocketProvider = new UdpSocketProvider();
        return udpStocketProvider;
    }
    /**
     * 关闭UDP连接
     */
    public void closeLocalUDPSocket() {
        try {
            if (this.socket != null) {
                this.socket.close();
                this.socket = null;
                return;
            }
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
    private DatagramSocket createSocket() {
        try {
            closeLocalUDPSocket();

            this.socket = new DatagramSocket(9434, InetAddress.getByName("0.0.0.0"));
            this.socket.setReuseAddress(true);
            return this.socket;
        } catch (Exception localException) {
            localException.printStackTrace();
            closeLocalUDPSocket();
        }
        return null;
    }

    public DatagramSocket getLocalUDPSocket(){
        if (socket != null && !socket.isClosed()) {
            return this.socket;
        }

        return createSocket();
    }
}