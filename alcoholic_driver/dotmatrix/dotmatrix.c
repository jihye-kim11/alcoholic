#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/uaccess.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>
#include <linux/delay.h>
#include <asm/ioctl.h>

#define DRIVER_AUTHOR   "Alcoholic"
#define DRIVER_DESC     "driver for dotmatrix"

#define DOTM_NAME               "dotmatrix"
#define DOTM_MODULES_VERSION    "dotmatrix V1.0"
#define DOTM_ADDR               0x210

#define DOTM_MAGIC              0xBC
#define DOTM_SET_CLEAR          _IOW(DOTM_MAGIC, 0, int)
#define DOTM_SPIN               _IOW(DOTM_MAGIC, 1, int)
#define DOTM_POP               _IOW(DOTM_MAGIC, 2, int)
#define DOTM_BOMB               _IOW(DOTM_MAGIC, 3, int)


#define DOT_WIDTH       8 
#define DOT_HEIGHT      10
unsigned char buffer[10] = {0, };
unsigned char current_word[10] = {0, };
int position = 0;
int word_len = 0;
unsigned char updown_buffer[10] = {0, };

extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);

static int dotm_in_use = 0;

unsigned char dotm_fontmap_spin[6][10] = {
        {0x7f,0x40,0x7f,0x08,0x7f,0x00,0x7f,0x0e,0x38,0x7f}, // dol
        {0x00,0x02,0x32,0x4a,0x4b,0x4a,0x32,0x02,0x00,0x00}, // ah
        {0x7a,0x0a,0x13,0x22,0x42,0x00,0x20,0x20,0x3e,0x00}, // gan
        {0x00,0x02,0x7a,0x42,0x43,0x42,0x7a,0x02,0x00,0x00}, // da
        {0x00,0x00,0x00,0x30,0x49,0x06,0x00,0x00,0x00,0x00}, // ~
        {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00}  // empty
};

unsigned char dotm_fontmap_pop[4][10] = {
        {0x00,0x00,0x00,0x00,0x08,0x00,0x00,0x00,0x00,0x00}, // dol
        {0x00,0x00,0x00,0x1c,0x14,0x1c,0x00,0x00,0x00,0x00}, // ah
        {0x00,0x00,0x3e,0x22,0x22,0x22,0x3e,0x00,0x00,0x00}, // gan
        {0x00,0x7f,0x41,0x41,0x41,0x41,0x41,0x7f,0x00,0x00}, // da
};

unsigned char dotm_fontmap_bomb[4][10] = {
        {0x00,0x00,0x00,0x00,0x08,0x00,0x00,0x00,0x00,0x00}, // dol
        {0x00,0x00,0x00,0x14,0x08,0x14,0x00,0x00,0x00,0x00}, // ah
        {0x00,0x00,0x08,0x14,0x2a,0x14,0x08,0x00,0x00,0x00}, // gan
        {0x00,0x14,0x08,0x55,0x2a,0x55,0x08,0x14,0x00,0x00}, // da
};


unsigned char dotm_fontmap_empty[10] = {
        0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00
};

void clearBuf(unsigned char *buffer, int len)
{
        int i;
        for(i=0; i<len; i++){
                buffer[i]=0;
        }
}


int dotm_open(struct inode *pinode, struct file *pfile)
{
        if (dotm_in_use != 0){
                return -EBUSY;
        }

        dotm_in_use = 1;

        return 0;
}

ssize_t dotm_write(struct file *pinode, const char *gdata, size_t len, loff_t *off_what)
{
        int ret, i;
        unsigned short wordvalue;
        const char *tmp = NULL;
        position=0;
        tmp = gdata;

        ret = copy_from_user(buffer, tmp, len);

        if (ret) {
                return -EFAULT;
        }

        for (i=0; i< 10; i++)
        {
                wordvalue = dotm_fontmap_empty[i] & 0x7F;
                iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
                current_word[i] = dotm_fontmap_empty[i] & 0x7F;
        }



        return len;
}

static long dotm_ioctl(struct file *pinode, unsigned int cmd, unsigned long data)
{
        int i;
        unsigned short wordvalue;
        unsigned char next_word[10];
        int next_word_index;

        switch (cmd){

        case DOTM_SET_CLEAR:
                for (i=0; i<10;i++){
                        wordvalue = dotm_fontmap_empty[i] & 0x7F;
                        iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
                }

                for (i=0; i<10; i++) {
                        updown_buffer[i] = 0;
                }

                break;

        case DOTM_SPIN:
                for(i=0;i<10;i++){
                        next_word[i] = 0;
                }

                for(i=0;i<10; i++){
                        current_word[i] <<= 1;
                        current_word[i] &= 0x7F;
                }

                if(position > 0 || (position / DOT_WIDTH) < word_len) {
                        next_word_index = (position / DOT_WIDTH);

                        if(position % DOT_WIDTH != 0){
                                for(i=0;i<10;i++){
                                        next_word[i] = dotm_fontmap_spin[buffer[next_word_index]][i];
                                        next_word[i] >>= DOT_WIDTH - (position % DOT_WIDTH)-1;
                                        next_word[i] &= 0x01;
                                }
                        }
                }

                for(i=0;i<10;i++){
                        current_word[i] = (current_word[i] | next_word[i]) & 0x7F;
                        wordvalue = current_word[i];
                        iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
                }
                position++;
                break;

        case DOTM_POP:
                for (i=0; i<10; i++){
                        wordvalue = dotm_fontmap_pop[data][i] & 0x7F;
                        iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
                }
                break;

        case DOTM_BOMB:
                for (i=0; i<10; i++){
                        wordvalue = dotm_fontmap_bomb[data][i] & 0x7F;
                        iom_fpga_itf_write((unsigned int) DOTM_ADDR+(i*2), wordvalue);
                }
                break;
        }
        return 0;
}

static struct file_operations dotm_fops = {
        .owner  = THIS_MODULE,
        .open   = dotm_open,
        .write  = dotm_write,
        .unlocked_ioctl = dotm_ioctl,
        .release= dotm_release,
};

static struct miscdevice dotm_driver = {
        .fops   = &dotm_fops,
        .name   = DOTM_NAME,
        .minor  = MISC_DYNAMIC_MINOR,
};

static int dot_init(void)
{
        misc_register(&dotm_driver);
        printk("init module dot\n");
        return 0;
}

static void dot_exit(void)
{
        misc_deregister(&dotm_driver);
        printk("exit module dot\n");
}

module_init(dot_init);
module_exit(dot_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");


