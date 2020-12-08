#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <signal.h>

#define DOTM_MAGIC              0xBC
#define DOTM_SET_CLEAR          _IOW(DOTM_MAGIC, 0, int)
#define DOTM_SPIN               _IOW(DOTM_MAGIC, 1, int)
#define DOTM_POP                _IOW(DOTM_MAGIC, 2, int)
#define DOTM_BOMB               _IOW(DOTM_MAGIC, 3, int)

unsigned char quit = 0;
void user_signal1(int sig)
{
        quit = 1;
}

int main(int argc, char** argv)
{
        int fd, i, j, size;
        int num;
        unsigned char buffer[6];

        fd = open("/dev/dotmatrix",O_RDWR);

        if (fd < 0){
                printf("Device Open Error\n");
                close(fd);
                return -1;
        }

        (void)signal(SIGINT, user_signal1);

        if(strcmp(argv[1], "spin") == 0) {
                for(i=0; i<6; i++) {
                        buffer[i] = i;
                }
                buffer[6]='\0';

                for(i=0; i<2; i++) {
                        write(fd, buffer, 5*sizeof(int));

                        for(j=0; j<48; j++) {
                                ioctl(fd, DOTM_SPIN, NULL, _IOC_SIZE(DOTM_SPIN));
                        }
                }
                ioctl(fd, DOTM_SET_CLEAR, NULL);
        }
        if(strcmp(argv[1], "pop") == 0) {
                for(i=0; i<4; i++){
                        ioctl(fd, DOTM_POP, i, _IOC_SIZE(DOTM_POP));
                }
                ioctl(fd, DOTM_SET_CLEAR, NULL);
        }
        if(strcmp(argv[1], "bomb") == 0) {
                for(i=0; i<4; i++){
                        ioctl(fd, DOTM_BOMB, i, _IOC_SIZE(DOTM_BOMB));
                }
        ioctl(fd, DOTM_SET_CLEAR, NULL);
        }

        close(fd);
}
