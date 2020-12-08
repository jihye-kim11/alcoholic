package com.company.my.alchoholic.sensor;

import android.widget.Button;

import com.company.my.alchoholic.sensor.iorequest.IoRequestProcessingThread;
import com.company.my.alchoholic.sensor.iorequest.Write7SegmentRequest;
import com.company.my.alchoholic.sensor.iorequest.WriteLcdRequest;
import com.company.my.alchoholic.sensor.iorequest.WriteLedRequest;
import com.company.my.alchoholic.sensor.iorequest.WriteMotorSpinRequest;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class SensorInstance implements Sensor{

    private SensorStatus status;
    private int[] fds = new int[8];
    private InputThread inputThread;
    private IoRequestProcessingThread ioThread;
    private Vector<Vector<ButtonCallback>> btnCallbacks;
    private Vector<Vector<ButtonCallback>> switchCallbacks;

    static {
        System.loadLibrary("sensor");
    }
    private native int loadSensors(int[] arr);
    private native int unloadSensors(int[] arr);
    private native int dotmSpin(int dotmFd);
    private native int dotmPop(int dotmFd);
    private native int dotmBomb(int dotmFd);
    private native int dotmClear(int dotmFd);

    private void init(){

        status = SensorStatus.READY;
        btnCallbacks = new Vector<>();
        for (int i = 0; i < Sensor.NUM_BTN; i++){
            btnCallbacks.add(new Vector<ButtonCallback>());
        }

        switchCallbacks = new Vector<>();
        for(int i = 0; i < Sensor.NUM_SWITCH; i++){
            switchCallbacks.add(new Vector<ButtonCallback>());
        }


    }

    @Override
    public void readySensor() {
        loadSensors(fds);
        boolean flag = true;
        for (int idx = 0; idx < fds.length; idx++) {
            System.out.println("idx " + idx + " : " + fds[idx]);
            if (fds[idx] == -1) {
                flag = false;
            }
        }
        if (flag) status = SensorStatus.START;
        else status = SensorStatus.FAIL;


        ioThread = new IoRequestProcessingThread();
        ioThread.start();
        inputThread = new InputThread(fds.clone(), btnCallbacks, switchCallbacks, ioThread);
        inputThread.start();
        System.out.println("Run Thread");
    }

    @Override
    public void stopSensor() {
        unloadSensors(fds);
    }

    @Override
    public SensorStatus getSensorStatus(){
        return status;
    }

    @Override
    public void runMotor(int direction, int speed) {}

    @Override
    public void runMotor(int direction, final int speed, final int time) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ioThread.addRequest(
                        new WriteMotorSpinRequest(
                                fds[SensorType.STEP_MOTOR.getSensorCode()],
                                1,
                                1,
                                speed
                        )
                );
                System.out.printf("register motor %d %d\n", speed, time);
                try {
                    Thread.sleep(time * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ioThread.addRequest(
                        new WriteMotorSpinRequest(
                                fds[SensorType.STEP_MOTOR.getSensorCode()],
                                0,
                                0,
                                0
                        )
                );
                System.out.println("register motor stop");
            }
        });
        thread.start();
    }

    @Override
    public void stopMotor() {}

    @Override
    public void startAnimatedDot(final int code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("DOTM Animation On " + code);
                if (code == 0) dotmSpin(fds[SensorType.DOT_MATRIX.getSensorCode()]);
                else if (code == 1) dotmPop(fds[SensorType.DOT_MATRIX.getSensorCode()]);
                else if (code == 2) dotmBomb(fds[SensorType.DOT_MATRIX.getSensorCode()]);
            }
        }).start();
    }

    @Override
    public void stopAnimatedDot() {}

    @Override
    public void clearDot() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dotmClear(fds[SensorType.DOT_MATRIX.getSensorCode()]);
            }
        }).start();
    }

    @Override
    public void registerButtonCallback(int btnNum, ButtonCallback callback) {
        btnCallbacks.get(btnNum).add(callback);
    }

    @Override
    public void unregisterButtonCallback(int btnNum, ButtonCallback callback) {
        btnCallbacks.get(btnNum).remove(callback);
    }

    @Override
    public void registerSwitchCallback(int switchNum, ButtonCallback callback) {
        switchCallbacks.get(switchNum).add(callback);
    }

    @Override
    public void unregisterSwitchCallback(int switchNum, ButtonCallback callback) {
        switchCallbacks.get(switchNum).remove(callback);
    }

    private int num7Seg[] = { 0, 0, 0, 0 };
    private void commit7Segment(){
        for (int idx = 0; idx < num7Seg.length; idx++) num7Seg[idx] = num7Seg[idx] > 9 ? 9 : num7Seg[idx];
        ioThread.addRequest(
                new Write7SegmentRequest(
                        fds[SensorType.SEVEN_SEG.getSensorCode()],
                        num7Seg[0], num7Seg[1], num7Seg[2], num7Seg[3]
                )
        );
    }

    @Override
    public void show7Seg(int num) {
        num7Seg[0] = num / 1000;
        num %= 1000;
        num7Seg[1] = num / 100;
        num %= 100;
        num7Seg[2] = num / 10;
        num %= 10;
        num7Seg[3] = num;
        commit7Segment();
    }

    @Override
    public void show7Seg(int digit1, int digit2, int digit3, int digit4) {
        num7Seg[0] = digit1;
        num7Seg[1] = digit2;
        num7Seg[2] = digit3;
        num7Seg[3] = digit4;
        commit7Segment();
    }

    @Override
    public void show7Seg(int pos, int digit) {
        num7Seg[pos] = digit;
        commit7Segment();
    }

    @Override
    public void showLed(int amount) {
        if (0 > amount) amount = 0;
        if (amount > 8) amount = 8;
        final int temp = amount;
        ioThread.addRequest(
                new WriteLedRequest(
                        fds[SensorType.LED.getSensorCode()],
                        temp
                )
        );
    }

    private String[] lines = { "", "" };
    private void commitLcd(){
        ioThread.addRequest(new WriteLcdRequest(
                fds[SensorType.LCD.getSensorCode()],
                lines[0],
                lines[1]
        ));
    }

    @Override
    public void showLcd(String line1, String line2) {
        String l1 = line1 == null ? "" : line1;
        String l2 = line2 == null ? "" : line2;
        lines[0] = l1;
        lines[1] = l2;
        commitLcd();
    }

    @Override
    public void showLcd(int lineNum, String line) {
        String l = line == null ? "" : line;
        if (lineNum < 0 || lineNum > 1) lineNum = 0;
        lines[lineNum] = line;
        commitLcd();
    }


    //
    // Singleton pattern
    //
    private SensorInstance() { init(); };
    private static SensorInstance instance;
    public static SensorInstance getInstance(){
        if (instance == null){
            instance = new SensorInstance();
        }
        return instance;
    }

}
