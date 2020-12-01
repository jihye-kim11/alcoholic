#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>
#include <sys/ioctl.h>
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
extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_dotmSpin(JNIEnv *env, jobject thiz, jint dotm_fd) {
    if (dotm_fd < 0) return -1;
    unsigned char buf[6] = { 0, 1, 2, 3, 4, 5 };
    for (int j = 0; j < 1; j ++){
        write(dotm_fd, buf, 6);
        for (int i = 0; i < 40; i++) ioctl(dotm_fd, DOTM_SPIN, NULL, _IOC_SIZE(DOTM_SPIN));
    }
    ioctl(dotm_fd, DOTM_SET_CLEAR, NULL, _IOC_SIZE(DOTM_SPIN));
    return 0;
}
extern "C" JNIEXPORT jint JNICALL
Java_com_company_my_alchoholic_sensor_SensorInstance_motorSpin(JNIEnv *env, jobject thiz, jint motor_fd, jint speed, jint second) {

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
Java_com_company_my_alchoholic_sensor_InputThread_readSwitchs(JNIEnv *env, jobject thiz,
                                                              jint switch_fd) {
    // TODO: implement readSwitchs()
}