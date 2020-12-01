#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>



int main(int argc, char **argv){

    int fd, i;
    unsigned char bytedata[4];
    unsigned char ret;

    bytedata[0] = atoi(argv[1]);
    bytedata[1] = atoi(argv[2]);
    bytedata[2] = atoi(argv[3]);
    bytedata[3] = atoi(argv[4]);

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

    close(fd);

    return 0;

}
