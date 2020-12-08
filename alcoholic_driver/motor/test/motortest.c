#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>

#define MOTOR_MAGIC              0xBC
#define MOTOR_SET_STOP          _IOW(MOTOR_MAGIC, 0, int)

int main(int argc, char **argv){

    int fd, i;
    unsigned int speed;
    unsigned int second;
    unsigned char ret;
    unsigned char bytedata[3]={1, 1, 10};

    speed = atoi(argv[1]);
    bytedata[2]=speed;
    second = atoi(argv[2]);


    fd = open("/dev/motor", O_RDWR);
    if (fd < 0) {
        printf("device open error : /dev/motor\n");
        return -1;
    }

    ret = write(fd, bytedata, 4);
    if (ret < 0){
        printf("write error!\n");
        return -1;
    }
    usleep(1000000*second);
    ioctl(fd, MOTOR_SET_STOP, NULL, _IOC_SIZE(MOTOR_SET_STOP));

    close(fd);

    return 0;

}
