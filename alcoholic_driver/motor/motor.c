#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/init.h>
#include <linux/uaccess.h>
#include <linux/fs.h>
#include <linux/miscdevice.h>
#include <linux/delay.h>

#define DRIVER_AUTHOR "Alcoholic"
#define DRIVER_DESC   "driver for stepmotor"

#define MOTOR_NAME                "motor"
#define MOTOR_VERSION             "motor V1.0"
#define MOTOR_ADDR_ACT            0x00C
#define MOTOR_ADDR_DIR            0x00E
#define MOTOR_ADDR_VEL            0x010

#define MOTOR_MAGIC     0xBC
#define MOTOR_SET_STOP  _IOW(MOTOR_MAGIC, 0, int)

//gpio fpga interface
extern ssize_t iom_fpga_itf_read(unsigned int addr);
extern ssize_t iom_fpga_itf_write(unsigned int addr, unsigned short int value);

//global
static int motor_in_use = 0;

int motor_open(struct inode *pinode, struct file *pfile)
{
        if (motor_in_use != 0)
                return -EBUSY;
        motor_in_use = 1; return 0;
}

int motor_release(struct inode *pinode, struct file *pfile)
{
        motor_in_use = 0;
        return 0;
}

ssize_t motor_write(struct file *pinode, const char *gdata, size_t len, loff_t *off_what){

    int ret;
    unsigned char input[4];
    unsigned short wordvalue;
    const char *tmp = gdata;

    ret = copy_from_user(input, tmp, 4);
    if (ret < 0) return -EFAULT;

    wordvalue = input[0] & 0xFF;
    iom_fpga_itf_write((unsigned int) MOTOR_ADDR_ACT, wordvalue);

    wordvalue = input[1] & 0xFF;
    iom_fpga_itf_write((unsigned int) MOTOR_ADDR_DIR, wordvalue);

    wordvalue = input[2] & 0xFF;
    iom_fpga_itf_write((unsigned int) MOTOR_ADDR_VEL, wordvalue);

    return len;
}

static long motor_ioctl(struct file *pinode, unsigned int cmd, unsigned long data)
{
        switch (cmd){

        case MOTOR_SET_STOP:
                iom_fpga_itf_write((unsigned int) MOTOR_ADDR_ACT, 0x00);
                break;
        }
        return 0;
}

static struct file_operations motor_fops = {
    .owner   = THIS_MODULE,
    .open    = motor_open,
    .write   = motor_write,
    .unlocked_ioctl = motor_ioctl,
    .release = motor_release,
};

static struct miscdevice motor_driver = {
    .fops  = &motor_fops,
    .name  = MOTOR_NAME,
    .minor = MISC_DYNAMIC_MINOR,
};


static int motor_init(void)
{
        misc_register(&motor_driver);
        printk(KERN_INFO "driver : %s driver init\n", MOTOR_NAME);
        return 0;
}

static void motor_exit(void)
{
        misc_deregister(&motor_driver);
        printk(KERN_INFO "driver: %s driver exit\n", MOTOR_NAME);
}

module_init(motor_init);
module_exit(motor_exit);

MODULE_AUTHOR(DRIVER_AUTHOR);
MODULE_DESCRIPTION(DRIVER_DESC);
MODULE_LICENSE("Dual BSD/GPL");