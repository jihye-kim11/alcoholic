#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main(void){

    int fd, fd2, i;
    unsigned char bytedata[4];
    unsigned char ret;
    unsigned char num[1];

    fd = open("/dev/motor", O_RDWR);
    fd2= open("/dev/switch", O_RDWR);
    if (fd < 0) {
        printf("device open error : /dev/motor\n");
        return -1;
    }

    read(fd2, num, 1);

    printf("%X", num[0]);
    bytedata[0] = atoi("1");
    bytedata[1] = atoi("1");
    bytedata[2] = atoi("8");
    bytedata[3] = num[0];

    ret = write(fd, bytedata, 4);
    if (ret < 0){
        printf("write error!\n");
        return -1;
    }

    close(fd);

    return 0;

}
