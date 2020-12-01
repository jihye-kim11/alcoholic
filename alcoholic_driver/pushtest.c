#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/ioctl.h>
#include <fcntl.h>
#include <signal.h>

#define MAX_BUTTON              9
#define DOTM_MAGIC              0xBC

unsigned char quit = 0;
void user_signal1(int sig)
{
        quit = 1;
}

int main(void)
{
        int fd, i,size;
        int num;
        unsigned char push_switch_buff[MAX_BUTTON];

        fd = open("/dev/push",O_RDWR);

        if (fd<0){
                printf("Device Open Error\n");
                close(fd);
                return -1;
        }
        (void)signal(SIGINT, user_signal1);
        size=sizeof(push_switch_buff);

        printf("Press <ctrl+c> to quit. \n");

        while(!quit) {
                usleep(10000);
                read(fd, &push_switch_buff, size);
                for(i=0;i<MAX_BUTTON;i++) {
                        if(push_switch_buff[i] == 1) {
                                num = i;
                        }
                }
                printf("%X\n", num);
        }
        close(fd);
}
