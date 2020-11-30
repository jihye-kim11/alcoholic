package com.company.my.alchoholic.sensor;

/**
 * 센서 드라이버들이 제대로 읽혔는지에 대한 상태값
 * READY, START, FAIL 세가지가 있다.
 */
public enum SensorStatus {

    /**
     * 센서가 아직 읽혀지지 않은 상태
     */
    READY,
    /**
     * 센서 드라이버를 전부 읽은 상태
     */
    START,
    /**
     * 센서 드라이버를 읽기 실패한 상태
     */
    FAIL,


}
