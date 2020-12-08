package com.company.my.alchoholic.sensor.iorequest;

public class WriteLcdRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int showLcd(int lcdFd, String jl1, String jl2);

    private int fd;
    private String l1, l2;
    public WriteLcdRequest(int fd, String l1, String l2){
        this.fd = fd;
        this.l1 = l1;
        this.l2 = l2;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        return showLcd(fd, l1, l2);
    }
}
