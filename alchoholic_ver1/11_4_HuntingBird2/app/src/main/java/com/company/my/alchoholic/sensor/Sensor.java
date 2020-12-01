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

    /**
     * 모터를 주어진 시간 동안 돌리는 메소드
     * @param direction
     * @param speed
     * @param time
     */
    public void runMotor(int direction, int speed, int time);
    public void runMotor(int direction, int speed);
    public void stopMotor();

    /**
     * 도트 매트릭스에 애니메이션을 보여주는 메소드.
     * code에 따라 다양한 애니메이션을 보여준다.
     * 현재는 무슨 code를 넣든 돌아간다 애니메이션만 호출한다.
     * @param code
     */
    public void startAnimatedDot(int code);
    public void stopAnimatedDot();
    public void clearDot();

    public void registerButtonCallback(ButtonCallback callback);
    public void unregisterButtonCallback(ButtonCallback callback);


}
