#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/ioctl.h>

#define LCD_MAGIC               0xBD
#define LCD_SET_CURSOR_POS      _IOW(LCD_MAGIC, 0, int)

int main(int argc, char **argv)
{
        int fd, pos;

        fd = open ("/dev/lcd", O_WRONLY);
        if(fd < 0) {
                fprintf(stderr, "error opening device\n");
                return 1;
        }

        pos = 0;
        ioctl(fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
        write(fd, argv[1], strlen(argv[1]));

        pos = 16;
        ioctl(fd, LCD_SET_CURSOR_POS, &pos, _IOC_SIZE(LCD_SET_CURSOR_POS));
        write(fd, argv[2], strlen(argv[2]));

        close(fd);

        return 0;
}
