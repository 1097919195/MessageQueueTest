package com.example.messagequeuetest.motherboardsocket;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.messagequeuetest.app.AppConstant;
import com.example.messagequeuetest.serialization.SerializeUtil;
import com.example.x6.serialportlib.SerialPort;


/*NFC*/
public class ReceiveNFCThread extends Thread {
    private SerialPort serialPort;
    private Handler handlers;
    private Message msg;
    private byte[] readData = new byte[1024];
    private String serialData = "";
    private boolean isReceiveLoop = true;//蓝牙主板读取监听是否死循环，退出后置为false，结束线程

    public void setIsReceiveLoop(boolean isReceiveLoop) {
        this.isReceiveLoop = isReceiveLoop;
    }

    public ReceiveNFCThread(SerialPort serialPort, Handler handler) {
        this.serialPort = serialPort;
        handlers = handler;
    }

    @Override
    public void run() {
        while (isReceiveLoop) {
            int size = serialPort.receiveData(readData);
            if (size > 0) {
                int tmpSize = size * 2;
                //这里的nfc的解析中02是字节头， 0D0A03是标记结尾，中间的全部是，这个也不会粘包啥的，即截取的时候直接过滤就好了(02 34323435303836393635 0D0A03)
                serialData = SerializeUtil.byteArrayToHexString(readData).substring(0, tmpSize);
                Log.e("test2", "NFC串口数据1:" + serialData);
                msg = new Message();
                msg.what = AppConstant.READ_NFC;
                msg.obj = serialData;
                handlers.sendMessage(msg);
            }
            if (!isReceiveLoop)
                colse();
        }
    }

    public void colse() {
        serialPort.closeSerial();
        handlers.removeCallbacksAndMessages(null);//清空消息队列保证不出现内存泄漏
    }
}
