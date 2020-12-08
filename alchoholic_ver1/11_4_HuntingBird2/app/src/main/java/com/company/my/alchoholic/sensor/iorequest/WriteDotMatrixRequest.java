package com.company.my.alchoholic.sensor.iorequest;

public class WriteDotMatrixRequest implements IoRequest{

    static {
        System.loadLibrary("sensor");
    }

    private native int showDotmAni(int fd, int code, int idx);
    private int fd, code, idx; // spin pop bomb clear
    private boolean skip;

    public WriteDotMatrixRequest(int fd, int code, int idx){
        this.fd = fd;
        this.code = code;
        this.idx = idx;
        this.skip = false;
    }


    public void setSkip(boolean skip){
        this.skip = skip;
    }

    @Override
    public boolean isSkipped() {
        return skip;
    }

    @Override
    public int processRequest() {

        long time = System.currentTimeMillis();
        int ret = showDotmAni(fd, code, idx);
        System.out.println("걸린시간 " + (System.currentTimeMillis() - time));
        return ret;
    }
}
