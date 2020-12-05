#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>

int main(int argc, char **argv)
{
        int fd;
        int num;
        unsigned char bytevalues[4];
        unsigned char ret;

        if(argc != 2) {
                printf("please input the parameter! \n");
                printf("ex)./7segment 1234\n");
                return -1;
        }

        num = atoi(argv[1]);
        if((num < 0) || (num > 9999)) {
                printf("Invalid range! \n");
                return -1;
        }

        fd = open("/dev/7segment", O_RDWR);
        if(fd < 0) {
                printf("Device open error : /dev/7segment\n");
                return -1;
        }

        bytevalues[0] = num / 1000;
        num = num % 1000;
        bytevalues[1] = num / 100;
        num = num % 100;
        bytevalues[2] = num / 10;
        num = num % 10;
        bytevalues[3] = num;

        ret = write(fd, bytevalues, 4);
        if (ret < 0) {
                printf("Write Error!\n");
                return -1;
        }

        close(fd);

        return 0;
}
