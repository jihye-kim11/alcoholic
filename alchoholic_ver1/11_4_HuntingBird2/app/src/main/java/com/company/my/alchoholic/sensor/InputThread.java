package com.company.my.alchoholic.sensor;

import java.util.List;
import java.util.Vector;

public class InputThread implements Runnable{

    private Thread thread;
    private int[] fds;
    private Vector<Vector<ButtonCallback>> btnCallbacks;
    private Vector<Vector<ButtonCallback>> switchCallbacks;

    static {
        System.loadLibrary("sensor");
    }
    private native int readBtns(int btnFd);
    private native int readSwitchs(int switchFd);

    public InputThread(int[] fds, Vector<Vector<ButtonCallback>> btnCallbacks, Vector<Vector<ButtonCallback>> switchCallbacks){
        this.thread = new Thread(this, "InputThread");
        this.fds = fds;
        this.btnCallbacks = btnCallbacks;
        this.switchCallbacks = switchCallbacks;
    }

    public void start(){
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            int rawBtnValue = readBtns(fds[SensorType.BUTTON.getSensorCode()]);
            for (int idx = 0; idx< Sensor.NUM_BTN; idx++){
                rawBtnValue = rawBtnValue >> 1;
                int btnValue = rawBtnValue & 0x01;
                for (ButtonCallback buttonCallback : btnCallbacks.get(idx)) {
                    buttonCallback.onButtonClick(btnValue);
                }
            }

            int rawSwitchValue = readSwitchs(fds[SensorType.SWITCH.getSensorCode()]);
            for (int idx = 0; idx< Sensor.NUM_SWITCH; idx++){
                int switchValue = rawSwitchValue & 0x01;
                for (ButtonCallback buttonCallback : switchCallbacks.get(idx)) {
                    buttonCallback.onButtonClick(switchValue);
                }
                rawSwitchValue = rawSwitchValue >> 1;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
