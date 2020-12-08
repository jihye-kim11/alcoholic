#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/uaccess.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>

#define DRIVER_AUTHOR   "CAUSW your name"
#define DRIVER_DESC     "driver for 8-SWITCHs FPGA"

#define SWITCH_NAME             "switch"
#define SWITCH_MODULE_VERSION   "SWITCH V1.0"
#define SWITCH_ADDR             0x000

//gpio fpga interface provided
extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);

//global
static int switch_in_use = 0;

int switch_open(struct inode *pinode, struct file *pfile)
{
        if(switch_in_use != 0) {
                return -EBUSY;
        }

        switch_in_use = 1;

        return 0;
}

int switch_release(struct inode *pinode, struct file *pfile)
{
        switch_in_use = 0;

        return 0;
}

ssize_t switch_read(struct file *pinode, char *gdata, size_t len, loff_t *off_what)
{
        unsigned char bytevalue;
        unsigned short wordvalue;
        char *tmp = NULL;

        tmp = gdata;

        wordvalue = iom_fpga_itf_read((unsigned int) SWITCH_ADDR);
        bytevalue = (unsigned char) wordvalue & 0x00FF;
        if(copy_to_user(tmp, &bytevalue, 1)){
                return -EFAULT;
        }

        return len;
}

static struct file_operations switch_fops ={
        .owner  = THIS_MODULE,
        .open   = switch_open,
        .read   = switch_read,
        .release= switch_release,
};

static struct miscdevice switch_driver = {
        .fops   = &switch_fops,
        .name   = SWITCH_NAME,
        .minor  = MISC_DYNAMIC_MINOR,
};

int switch_init(void)
{
        misc_register(&switch_driver);
        printk(KERN_INFO "driver: %s driver init\n", SWITCH_NAME);

        return 0;
}

void switch_exit(void)
{
        misc_deregister(&switch_driver);
        printk(KERN_INFO "driver: %s driver exit\n", SWITCH_NAME);
}

module_init(switch_init);
module_exit(switch_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/gPL");
