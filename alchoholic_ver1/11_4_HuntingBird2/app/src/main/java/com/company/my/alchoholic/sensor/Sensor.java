package com.company.my.alchoholic.sensor;

public interface Sensor {

    /**
     * 센서 드라이버를 읽는 메소드
     */
    public void readySensor();

    /**
     * 센서 드라이버를 close 시켜주는 메소드.
     */
    public void stopSensor();

    /**
     * 현재 센서 드라이버를 읽은 상태를 의미한다.
     * @see SensorStatus
     * @return SensorStatus
     */
    public SensorStatus getSensorStatus();

    public void runMotor(int direction, int speed);
    public void runMotor(int direction, int speed, int time);
    public void stopMotor();

    public void startAnimatedDot(int code);
    public void stopAnimatedDot();
    public void clearDot();

    public void registerButtonCallback(ButtonCallback callback);
    public void unregisterButtonCallback(ButtonCallback callback);


}
