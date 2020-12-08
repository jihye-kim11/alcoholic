package com.company.my.alchoholic.sensor.iorequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IoRequestProcessingThread implements Runnable{

    public static final int REQ_DELAY = 3;

    private Thread thread;
    private Queue<IoRequest> requests;
    private volatile boolean runFlag;

    public IoRequestProcessingThread(){
        thread = new Thread(this);
        requests = new ConcurrentLinkedQueue<>();
    }

    public void start(){
        runFlag = true;
        thread.setDaemon(true);
        thread.start();
    }

    public void stop(){
        runFlag = false;
    }

    public void addRequest(IoRequest req){
        requests.add(req);
    }

    @Override
    public void run() {
        while(runFlag){
            IoRequest request = requests.poll();
            if (request != null && request.isSkipped() != true){
                long time = System.currentTimeMillis();
                request.processRequest();
                int size = requests.size();
                long timeDelta = (System.currentTimeMillis() - time);
                //System.out.printf("processing request %d %d\n", size, timeDelta);
                if (REQ_DELAY - timeDelta > 0 && size < 10){
                    try {
                        Thread.sleep(REQ_DELAY - timeDelta);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        runFlag = false;
                    }
                }
            }
        }
        System.out.println("IoRequestPorcessingThread가 종료되었습니다.");
    }
}
