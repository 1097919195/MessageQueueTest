package com.example.messagequeuetest.motherboardsocket;

/**
 * Created by Administrator on 2019/9/9.
 */

import android.os.Handler;

import com.example.x6.serialportlib.SerialPort;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消息队列缓冲定义
 *
 * @author hp
 */
public class PushBlockQueue {

    private static final long serialVersionUID = -8224792866430647454L;
    private static ExecutorService es = Executors.newFixedThreadPool(1);//线程池
    private static PushBlockQueue pbq = new PushBlockQueue();//单例
    public static MyStack<String> stack = new MyStack<String>();
    private boolean flag = false;

    private PushBlockQueue() {
    }

    public static PushBlockQueue getInstance() {
        return pbq;
    }

    /**
     * 队列监听启动
     */
    public void start(SerialPort serialPort, Handler handler) {
        if (!this.flag) {
            this.flag = true;
        } else {
            throw new IllegalArgumentException("队列已处于启动状态,不允许重复启动.");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (flag) {
                    //将获取消息交由线程池处理
                    if (stack.peek() != null) {//先这样判断可以节约资源内存
                        es.execute(new PushBlockQueueHandler(stack.poll(), serialPort, handler));
                    }
                }
            }
        }).start();

    }

    /**
     * 停止队列监听
     */
    public void stop() {
        this.flag = false;
    }
}
