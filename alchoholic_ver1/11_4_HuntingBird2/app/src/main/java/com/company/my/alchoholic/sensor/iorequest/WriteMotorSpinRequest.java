package com.company.my.alchoholic.sensor.iorequest;

public class WriteMotorSpinRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int spinMotor(int fd, int activate, int dir, int speed);

    private int fd;
    private int activate, dir, speed;

    public WriteMotorSpinRequest(int fd, int speed){
        this(fd, 1, 1, speed);
    }

    public WriteMotorSpinRequest(int fd, int dir, int speed){
        this(fd, 1, dir, speed);
    }

    public WriteMotorSpinRequest(int fd, int activate, int dir, int speed){
        this.fd = fd;
        this.activate = activate;
        this.dir = dir;
        this.speed = speed;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        return spinMotor(fd, activate, dir, speed);
    }
}
