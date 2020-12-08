package com.company.my.alchoholic.sensor.iorequest;

import com.company.my.alchoholic.sensor.ButtonCallback;
import com.company.my.alchoholic.sensor.Sensor;
import com.company.my.alchoholic.sensor.SensorType;

import java.util.Vector;

public class ReadSwitchRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }
    private native int readSwitchs(int btnFd);

    private Vector<Vector<ButtonCallback>> switchCallbacks;
    private int switchFd;
    public ReadSwitchRequest(int btnFd, Vector<Vector<ButtonCallback>> switchCallbacks){
        this.switchFd = btnFd;
        this.switchCallbacks = switchCallbacks;
    }

    @Override
    public boolean isSkipped() {
        return false;
    }

    @Override
    public int processRequest() {
        int rawSwitchValue = readSwitchs(switchFd);
        for (int idx = 0; idx< Sensor.NUM_SWITCH; idx++){
            int switchValue = rawSwitchValue & 0x01;
            for (ButtonCallback buttonCallback : switchCallbacks.get(idx)) {
                buttonCallback.onButtonClick(switchValue);
            }
            rawSwitchValue = rawSwitchValue >> 1;
        }
        return 0;
    }
}
