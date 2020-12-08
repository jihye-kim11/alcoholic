package com.company.my.alchoholic.sensor.iorequest;

public class Write7SegmentRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int show7Segment(int segFd, int d1, int d2, int d3, int d4);

    private int fd;
    private int num1, num2, num3, num4;
    public Write7SegmentRequest(int fd, int num1, int num2, int num3, int num4){
        this.fd = fd;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.num4 = num4;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        return show7Segment(fd, num1, num2, num3, num4);
    }
}
