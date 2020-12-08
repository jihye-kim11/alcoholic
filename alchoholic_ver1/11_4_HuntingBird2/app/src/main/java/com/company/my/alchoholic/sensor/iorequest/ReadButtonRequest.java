package com.company.my.alchoholic.sensor.iorequest;

import com.company.my.alchoholic.sensor.ButtonCallback;
import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorType;

import java.util.Vector;

public class ReadButtonRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int readBtns(int btnFd);

    private Vector<Vector<ButtonCallback>> btnCallbacks;
    private int btnFd;
    public ReadButtonRequest(int btnFd, Vector<Vector<ButtonCallback>> btnCallbacks){
        this.btnFd = btnFd;
        this.btnCallbacks = btnCallbacks;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        int rawBtnValue = readBtns(btnFd);
        for (int idx = 0; idx< Sensor.NUM_BTN; idx++){
            rawBtnValue = rawBtnValue >> 1;
            int btnValue = rawBtnValue & 0x01;
            for (ButtonCallback buttonCallback : btnCallbacks.get(idx)) {
                buttonCallback.onButtonClick(btnValue);
            }
        }
        return 0;
    }
}
