#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <math.h>

int main(int argc, char **argv)
{
    int fd;
    unsigned char bytedata;
    unsigned char ret;
    int i, j, num = 1, pow=1;

    if(argc != 2) {
        printf("please input the parameter! \n");
        printf("ex)./test_led 7 (0~255)\n");
        return -1;
    }

    bytedata = atoi(argv[1]);
    if((bytedata < 0) || (bytedata > 0xff)) {
        printf("Invalid range!\n");
        return -1;
    }

    fd = open("/dev/led", O_RDWR);
    if(fd<0) {
        printf("Device open error : /dev/led\n");
        return -1;
    }

    if(bytedata != 0) {
        for(i=1; i<bytedata; i++) {
                pow = 1;
                for(j=0;j<i;j++) {
                        pow *= 2;
                }
                num += pow;
            }
    bytedata = num;
    }

    ret = write(fd, &bytedata, 1);
    if(ret < 0) {
        printf("Write Error!\n");
        return -1;
    }

    sleep(1);

    bytedata = 0;
    ret = read(fd, &bytedata, 1);
    if(ret < 0) {
        printf("Read Error!\n");
        return -1;
    }

    printf("Current LED Value : 0x%x\n", bytedata);

    printf("\n");
    close(fd);

    return 0;
}
