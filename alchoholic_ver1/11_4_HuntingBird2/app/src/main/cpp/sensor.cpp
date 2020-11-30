#include <jni.h>
#include <stdio.h>
#include <unistd.h>
#include <fcntl.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <android/log.h>
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
    buf[2] = open("/dev/dot_matrix", O_RDWR);
    buf[3] = open("/dev/lcd", O_RDWR);
    buf[4] = open("/dev/button", O_RDWR);
    buf[5] = open("/dev/stepmotor", O_RDWR);
    buf[6] = open("/dev/buzzer", O_RDWR);
    buf[7] = open("/dev/switch", O_RDWR);
    env->ReleaseIntArrayElements(arr, buf, 0);
    return 0;
}

extern "C"
JNIEXPORT jint JNICALL
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