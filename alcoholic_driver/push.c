#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/uaccess.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>
#include <asm/io.h>

#define DRIVER_AUTHOR   "CAUSW your name"
#define DRIVER_DESC     "driver for PBUTTON"

#define PUSH_NAME               "push"
#define PUSH_MODULE_VERSION     "PBUTTON V1.0"
#define PUSH_ADDR               0x050

extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);

static int push_in_use = 0;

int push_open(struct inode *pinode, struct file *pfile)
{
        if(push_in_use != 0) {
                return -EBUSY;
        }

        push_in_use = 1;

        return 0;
}

int push_release(struct inode *pinode, struct file *pfile)
{
        push_in_use = 0;

        return 0;
}

ssize_t push_read(struct file *pinode, char *gdata, size_t len, loff_t *off_what)
{
        int i;
        unsigned char bytevalue[9];
        unsigned short wordvalue;
        char *tmp = NULL;
        tmp = gdata;

        for(i=0; i<9; i++) {
                wordvalue = iom_fpga_itf_read((unsigned int)PUSH_ADDR + i*2);
                bytevalue[i] = wordvalue & 0xFF;
        }

        if(copy_to_user(tmp, &bytevalue, 9)){
                return -EFAULT;
        }

        return len;
}

static struct file_operations push_fops ={
        .owner  = THIS_MODULE,
        .open   = push_open,
        .read   = push_read,
        .release= push_release,
};

static struct miscdevice push_driver = {
        .fops   = &push_fops,
        .name   = PUSH_NAME,
        .minor  = MISC_DYNAMIC_MINOR,
};

int push_init(void)
{
        misc_register(&push_driver);
        printk(KERN_INFO "driver: %s driver init\n", PUSH_NAME);

        return 0;
}

void push_exit(void)
{
        misc_deregister(&push_driver);
        printk(KERN_INFO "driver: %s driver exit\n", PUSH_NAME);
}

module_init(push_init);
module_exit(push_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");
