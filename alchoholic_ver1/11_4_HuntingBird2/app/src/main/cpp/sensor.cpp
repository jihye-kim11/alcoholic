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

    buf = env->GetIntArrayElements(arr, JNI_FALSE);
    buf[0] = open("/dev/led", O_RDWR);
    buf[1] = open("/dev/7segment", O_RDWR);
    buf[2] = open("/dev/dotmatrix", O_RDWR);
    buf[3] = open("/dev/lcd", O_RDWR);
    buf[4] = open("/dev/push", O_RDWR);
    buf[5] = open("/dev/motor", O_RDWR);
    buf[6] = open("/dev/buzzer", O_RDWR);
    buf[7] = open("/dev/switch", O_RDWR);
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
Java_com_company_my_alchoholic_sensor_SensorInstance_dotmSpin(JNIEnv *env, jobject thiz, jint dotm_fd) {
    if (dotm_fd < 0) return -1;
    __android_log_print(ANDROID_LOG_DEBUG, "Test", "enter dotmSpin");
    unsigned char buf[6] = { 0, 1, 2, 3, 4, 5 };
    for (int j = 0; j < 2; j ++){
        write(dotm_fd, buf, 6);
        for (int i = 0; i < 40; i++) {
            ioctl(dotm_fd, DOTM_SPIN, NULL, _IOC_SIZE(DOTM_SPIN));
            usleep(1000*50);
        }
    }
    ioctl(dotm_fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SPIN));
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_dotmPop(JNIEnv *env, jobject thiz, jint dotm_fd) {
    if (dotm_fd < 0) return -1;
    __android_log_print(ANDROID_LOG_DEBUG, "Test", "enter dotmSpin");
    unsigned char buf[6] = { 0, 1, 2, 3, 4, 5 };
    for (int j = 0; j < 2; j ++){
        write(dotm_fd, buf, 6);
        for (int i = 0; i < 40; i++) {
            ioctl(dotm_fd, DOTM_POP, NULL, _IOC_SIZE(DOTM_SPIN));
            usleep(1000*50);
        }
    }
    ioctl(dotm_fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SPIN));
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_dotmBomb(JNIEnv *env, jobject thiz, jint dotm_fd) {
    if (dotm_fd < 0) return -1;
    __android_log_print(ANDROID_LOG_DEBUG, "Test", "enter dotmSpin");
    unsigned char buf[6] = { 0, 1, 2, 3, 4, 5 };
    for (int j = 0; j < 2; j ++){
        write(dotm_fd, buf, 6);
        for (int i = 0; i < 40; i++) {
            ioctl(dotm_fd, DOTM_BOMB, NULL, _IOC_SIZE(DOTM_SPIN));
            usleep(1000*50);
        }
    }
    ioctl(dotm_fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SPIN));
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_motorSpin
(JNIEnv *env, jobject thiz, jint motor_fd, jint speed, jint second) {

    unsigned char buf[4] = { 1, 1, static_cast<unsigned char>(speed), static_cast<unsigned char>(second) };
    write(motor_fd, buf, 4);
    return 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_InputThread_readBtns(JNIEnv *env, jobject thiz, jint btn_fd) {
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
Java_com_company_my_alchoholic_sensor_InputThread_readSwitchs(JNIEnv *env, jobject thiz, jint switch_fd) {
    unsigned char byteData = 0;
    read(switch_fd, &byteData, 1);
    //__android_log_print(ANDROID_LOG_DEBUG, "Test", "switch input %d", byteData);
    return byteData;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_show7Segment(JNIEnv *env, jobject thiz, jint seg_fd, jint d1, jint d2, jint d3, jint d4) {
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
Java_com_company_my_alchoholic_sensor_SensorInstance_showLed(JNIEnv *env, jobject thiz, jint led_fd, jint amount) {
    if (led_fd < 0) return -1;
    unsigned char data = static_cast<unsigned char>(amount);
    int i,pow, j, num;
    if(data != 0) {
        for(i=0; i<data; i++) {
            pow = 1;
            for(j=0;j<i;j++) {
                pow *= 2;
            }
            num += pow;
        }
        data = num;
    }
    unsigned char bytevalue[1] = { data };
    write(led_fd, bytevalue, 1);
    return 0;
}

#define LCD_MAGIC 0xBD
#define LCD_SET_CURSOR_POS _IOW(LCD_MAGIC,0,int)

extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_showLcd(JNIEnv *env, jobject thiz, jint lcd_fd, jstring jl1, jstring jl2) {
    if (lcd_fd < 0) return -1;
    int pos = 0;
    const char* l1 = env->GetStringUTFChars(jl1, 0);
    ioctl(lcd_fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(lcd_fd, l1, strlen(l1));
    env->ReleaseStringUTFChars(jl1, l1);

    pos = 16;
    const char* l2 = env->GetStringUTFChars(jl2, 0);
    ioctl(lcd_fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
    write(lcd_fd, l2, strlen(l2));
    env->ReleaseStringUTFChars(jl2, l2);
    return 0;
}