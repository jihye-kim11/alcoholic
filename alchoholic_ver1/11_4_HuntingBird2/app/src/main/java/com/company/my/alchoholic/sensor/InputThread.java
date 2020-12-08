package com.company.my.alchoholic.sensor;

import com.company.my.alchoholic.sensor.iorequest.IoRequestProcessingThread;
import com.company.my.alchoholic.sensor.iorequest.ReadButtonRequest;
import com.company.my.alchoholic.sensor.iorequest.ReadSwitchRequest;

import java.util.List;
import java.util.Vector;

public class InputThread implements Runnable{

    private Thread thread;
    private int[] fds;
    private Vector<Vector<ButtonCallback>> btnCallbacks;
    private Vector<Vector<ButtonCallback>> switchCallbacks;
    private IoRequestProcessingThread ioReqThread;

    public InputThread(
            int[] fds,
            Vector<Vector<ButtonCallback>> btnCallbacks,
            Vector<Vector<ButtonCallback>> switchCallbacks,
            IoRequestProcessingThread ioReqThread){
        this.thread = new Thread(this, "InputThread");
        this.fds = fds;
        this.btnCallbacks = btnCallbacks;
        this.switchCallbacks = switchCallbacks;
        this.ioReqThread = ioReqThread;
    }

    public void start(){
        thread.start();
    }

    @Override
    public void run() {
        while(true){
            ioReqThread.addRequest(
                    new ReadButtonRequest(
                            fds[SensorType.BUTTON.getSensorCode()],
                            this.btnCallbacks
                    )
            );

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ioReqThread.addRequest(
                    new ReadSwitchRequest(
                            fds[SensorType.SWITCH.getSensorCode()],
                            this.switchCallbacks
                    )
            );

            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
