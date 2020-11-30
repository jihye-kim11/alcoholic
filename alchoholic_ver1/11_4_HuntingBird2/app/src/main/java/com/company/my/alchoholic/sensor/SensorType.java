package com.company.my.alchoholic.sensor;

public enum SensorType {

    LED(0),
    SEVEN_SEG(1),
    DOT_MATRIX(2),
    LCD(3),
    BUTTON(4),
    STEP_MOTOR(5),
    BUZZER(6),
    SWITCH(7);


    private int sensorCode;
    SensorType(int code){
        this.sensorCode = code;
    }

    int getSensorCode(){
        return this.sensorCode;
    }

}
