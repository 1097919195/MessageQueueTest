package com.example.messagequeuetest.motherboardsocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.messagequeuetest.app.AppConstant;
import com.example.messagequeuetest.serialization.SerializeUtil;
import com.example.x6.serialportlib.SerialPort;

/**
 * 队列消息处理实现
 *
 * @author hp
 */
public class PushBlockQueueHandler implements Runnable {

    private Object obj;
    SerialPort serialPort;
    Handler handlers;
    Message msg;
    private byte[] readData = new byte[1024];
    String serialData = "";

    public PushBlockQueueHandler(Object obj, SerialPort serialPort, Handler handler) {
        this.obj = obj;
        this.serialPort = serialPort;
        this.handlers = handler;
    }

    @Override
    public void run() {
        doBusiness();
    }

    /**
     * 业务处理时限
     */
    public void doBusiness() {
        Log.e("处理请求:", obj.toString());
        try {
            serialPort.sendData(obj.toString(), "HEX");
            Thread.sleep(200);//发完等待一下，直接读取可能会存在内存中
            int size = serialPort.receiveData(readData);
            if (size > 0) {
                int tmpSize = size * 2;
                serialData = SerializeUtil.byteArrayToHexString(readData).substring(0, tmpSize);
                Log.e("test", "接收到串口数据:" + serialData);
                msg = new Message();
                msg.what = AppConstant.READ_TIME_CODE;
                msg.obj = serialData;
                handlers.sendMessage(msg);
            }
            obj = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
