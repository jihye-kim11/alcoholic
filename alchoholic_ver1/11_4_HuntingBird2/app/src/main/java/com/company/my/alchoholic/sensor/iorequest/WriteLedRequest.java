package com.company.my.alchoholic.sensor.iorequest;

import com.company.my.alchoholic.sensor.SensorType;

public class WriteLedRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int showLed(int ledFd, int amount);

    private int fd;
    private int amount;
    public WriteLedRequest(int fd, int amount){
        this.fd = fd;
        this.amount = amount;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        System.out.println("led start!");
        return showLed(fd, amount);
    }
}
