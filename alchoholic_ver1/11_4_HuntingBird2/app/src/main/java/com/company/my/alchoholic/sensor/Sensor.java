package com.company.my.alchoholic.sensor;

public interface Sensor {

    public static final int NUM_BTN = 9;
    public static final int NUM_SWITCH = 8;

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
     * @param code 0 은 돌아간다, 1은 팝, 2는 폭탄 애니메이션이다.
     */
    public void startAnimatedDot(int code);
    public void stopAnimatedDot();
    public void clearDot();

    /**
     * 버튼 콜백을 등록하는 메소드. 오른쪽 위부터 876, 543, 321 이다.
     * @see ButtonCallback
     * @param btnNum 몇 번 버튼인지
     * @param callback
     */
    public void registerButtonCallback(int btnNum, ButtonCallback callback);

    /**
     * 버튼 콜백을 해체하는 메소드. 반드시 호출해줘야 버그가 안난다.
     * @param btnNum
     * @param callback
     */
    public void unregisterButtonCallback(int btnNum, ButtonCallback callback);

    /**
     * 스위치 콜백을 등록하는 메소드. 특정 시간마다 계속 호출된다.
     * @see ButtonCallback
     * @param switchNum
     * @param callback
     */
    public void registerSwitchCallback(int switchNum, ButtonCallback callback);

    /**
     * 스위치 콜백을 해체하는 메소드.
     * @param switchNum
     * @param callback
     */
    public void unregisterSwitchCallback(int switchNum, ButtonCallback callback);

    /**
     * 해당하는 숫자를 입력. 만약 해당 자리에 9를 초과하게 되면, 9로 나오게 된다.
     * @param num
     */
    public void show7Seg(int num);
    
    /**
     * 각각 해당하는 자리에 숫자를 입력. 만약 해당 자리에 9를 초과하게 되면, 9로 나오게 된다.
     * @param digit1
     * @param digit2
     * @param digit3
     * @param digit4
     */
    public void show7Seg(int digit1, int digit2, int digit3, int digit4);

    /**
     * 해당 자리에 해당 숫자를 입력. 만약 해당 자리에 9를 초과하게 되면, 9로 나오게 된다.
     * @param pos
     * @param digit
     */
    public void show7Seg(int pos, int digit);
    
    
}
