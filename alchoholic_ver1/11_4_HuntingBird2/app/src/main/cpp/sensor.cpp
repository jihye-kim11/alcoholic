#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>
#include <sys/ioctl.h>
#include <string.h>
//
// Created by jomingyu on 2020-11-30.
//

/*
    LED(0),
    SEVEN_SEG(1),
    DOT_MATRIX(2),
    LCD(3),
    BUTTON(4),
    STEP_MOTOR(5),
    BUZZER(6),
    SWITCH(7);
*/

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_loadSensors(JNIEnv *env, jobject thiz, jintArray arr){

    jint* buf;

    int flags = O_RDWR;
    buf = env->GetIntArrayElements(arr, JNI_FALSE);
    buf[0] = open("/dev/led", flags);
    buf[1] = open("/dev/7segment", flags);
    buf[2] = open("/dev/dotmatrix", flags);
    buf[3] = open("/dev/lcd", flags);
    buf[4] = open("/dev/push", flags);
    buf[5] = open("/dev/motor", flags);
    buf[6] = 0;
    buf[7] = open("/dev/switch", flags);
    env->ReleaseIntArrayElements(arr, buf, 0);
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_unloadSensors(JNIEnv *env, jobject thiz, jintArray arr) {

    //TODO: 성능향상을 위해 copy, commit 할지 여부를 결정할 것. 큰 의미는 없을 것 같긴함.
    jint* buf;
    jsize len;

    buf = env->GetIntArrayElements(arr, JNI_FALSE);
    len = env->GetArrayLength(arr);

    for (int idx = 0; idx < len; idx++){
        if (buf[idx] > 0) close(buf[idx]);
    }
    env->ReleaseIntArrayElements(arr, buf, 0);
    return 0;
}

#define DOTM_SET_CLEAR _IOW(0xBC, 0, int)
#define DOTM_SPIN      _IOW(0xBC, 1, int)
#define DOTM_POP       _IOW(0xBC, 2, int)
#define DOTM_BOMB      _IOW(0xBC, 3, int)

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_WriteDotMatrixRequest_showDotmAni(JNIEnv *env, jobject thiz, jint fd, jint code, jint idx) {
    if (fd < 0) return -1;
    __android_log_print(ANDROID_LOG_DEBUG, "showDotmAni", "enter dotm Pop");
    int _idx = idx;
    switch(code){
        case 0:
            ioctl(fd, DOTM_SPIN, _idx, _IOC_SIZE(DOTM_SPIN));
            break;
        case 1:
            ioctl(fd, DOTM_POP, _idx, _IOC_SIZE(DOTM_POP));
            break;
        case 2:
            ioctl(fd, DOTM_BOMB, _idx, _IOC_SIZE(DOTM_BOMB));
            break;
        default:
            ioctl(fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SET_CLEAR));
            break;
    }
    __android_log_print(ANDROID_LOG_DEBUG, "showDotmAni", "done dotm Pop");
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_WriteMotorSpinRequest_spinMotor
(JNIEnv *env, jobject thiz, jint motor_fd, jint activate, jint dir, jint speed) {
    __android_log_print(ANDROID_LOG_DEBUG, "spinMotor", "enter spinMotor %d %d %d %d", motor_fd, activate, dir, speed);
    unsigned char buf[3] = { static_cast<unsigned char>(activate), static_cast<unsigned char>(dir), static_cast<unsigned char>(speed) };
    write(motor_fd, buf, 3);
    __android_log_print(ANDROID_LOG_DEBUG, "spinMotor", "done spinMotor");
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_ReadButtonRequest_readBtns(JNIEnv *env, jobject thiz, jint btn_fd) {
    int result = 0;
    unsigned char readData[9] = {0, };
    read(btn_fd, readData, 9);
    for (int i = 0; i < 9; i++){
        result += readData[i];
        result = result << 1;
    }
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_ReadSwitchRequest_readSwitchs(JNIEnv *env, jobject thiz, jint switch_fd) {
    unsigned char byteData = 0;
    read(switch_fd, &byteData, 1);
    //__android_log_print(ANDROID_LOG_DEBUG, "Test", "switch input %d", byteData);
    return byteData;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_Write7SegmentRequest_show7Segment(JNIEnv *env, jobject thiz, jint seg_fd, jint d1, jint d2, jint d3, jint d4) {
    if (seg_fd < 0) return -1;
    unsigned char bytevalue[4] = {
            static_cast<unsigned char>(d1),
            static_cast<unsigned char>(d2),
            static_cast<unsigned char>(d3),
            static_cast<unsigned char>(d4)
    };
    write(seg_fd, bytevalue, 4);
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_WriteLedRequest_showLed(JNIEnv *env, jobject thiz, jint led_fd, jint amount) {
    if (led_fd < 0) return -1;
    unsigned char data = static_cast<unsigned char>(amount);
    __android_log_print(ANDROID_LOG_DEBUG, "showLed", "enter showLed %d", data);
    if (data != 0){
        int num = 1;
        for(int i=1; i<data; i++) {
            num = num << 1;
            num += 1;
        }
        data = num;
    }
    unsigned char bytevalue[1] = { data };
    __android_log_print(ANDROID_LOG_DEBUG, "showLed", "processing showLed %d", data);
    write(led_fd, bytevalue, 1);
    __android_log_print(ANDROID_LOG_DEBUG, "showLed", "done showLed %d", amount);
    return 0;
}

#define LCD_MAGIC 0xBD
#define LCD_SET_CURSOR_POS _IOW(LCD_MAGIC,0,int)

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_iorequest_WriteLcdRequest_showLcd(JNIEnv *env, jobject thiz, jint lcd_fd, jstring jl1, jstring jl2) {
    if (lcd_fd < 0) return -1;
    int pos = 0;
    const char* l1 = env->GetStringUTFChars(jl1, 0);
    __android_log_print(ANDROID_LOG_DEBUG, "showLcd", "line 1 [%s], %d", l1, strlen(l1));
    ioctl(lcd_fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(lcd_fd, l1, strlen(l1));
    env->ReleaseStringUTFChars(jl1, l1);

    pos = 16;
    const char* l2 = env->GetStringUTFChars(jl2, 0);
    __android_log_print(ANDROID_LOG_DEBUG, "showLcd", "line 2 [%s], %d", l2, strlen(l2));
    ioctl(lcd_fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(lcd_fd, l2, strlen(l2));
    env->ReleaseStringUTFChars(jl2, l2);
    return 0;
}