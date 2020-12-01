package com.company.my.alchoholic.sensor;

public class SensorInstance implements Sensor{

    private SensorStatus status;
    private int[] fds = new int[8];

    static {
        System.loadLibrary("sensor");
    }
    private native int loadSensors(int[] arr);
    private native int unloadSensors(int[] arr);
    private native int dotmSpin(int dotmFd);
    private native int motorSpin(int motorFd, int speed, int second);


    @Override
    public void readySensor() {
        loadSensors(fds);
        boolean flag = true;
        status = SensorStatus.START;
        for (int idx = 0; idx < fds.length; idx++) {
            System.out.println("idx " + idx + " : " + fds[idx]);
            if (fds[idx] == -1) {
                flag = false;
            }
        }
        if (flag) status = SensorStatus.START;
        else status = SensorStatus.FAIL;
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
    public void runMotor(int direction, int speed) {

    }

    @Override
    public void runMotor(int direction, int speed, int time) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("MOTOR On");
                motorSpin(fds[SensorType.STEP_MOTOR.getSensorCode()], 10, 5);
            }
        }).start();
    }

    @Override
    public void stopMotor() {

    }

    @Override
    public void startAnimatedDot(int code) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("DOTM Animation On");
                dotmSpin(fds[SensorType.DOT_MATRIX.getSensorCode()]);
            }
        }).start();
    }

    @Override
    public void stopAnimatedDot() {

    }

    @Override
    public void clearDot() {

    }

    @Override
    public void registerButtonCallback(ButtonCallback callback) {

    }

    @Override
    public void unregisterButtonCallback(ButtonCallback callback) {

    }


    //
    // Singleton pattern
    //
    private SensorInstance() {
        this.status = SensorStatus.READY;
    };
    private static SensorInstance instance;
    public static SensorInstance getInstance(){
        if (instance == null){
            instance = new SensorInstance();
        }
        return instance;
    }

}
