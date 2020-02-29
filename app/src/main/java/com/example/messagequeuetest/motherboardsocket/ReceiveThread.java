package com.example.messagequeuetest.motherboardsocket;

import android.os.Handler;
import android.os.Message;

import com.example.x6.serialportlib.SerialPort;


/*主板串口读写*/
public class ReceiveThread extends Thread {
    private SerialPort serialPort;
    Handler handlers;
    Message msg;
    byte[] readData = new byte[1024];
    String serialData = "";
    boolean isReceiveLoop = true;//蓝牙主板读取监听是否死循环，退出后置为false，结束线程

    public ReceiveThread(SerialPort serialPort, Handler handler) {
        this.serialPort = serialPort;
        handlers = handler;
    }

    @Override
    public void run() {
        while (isReceiveLoop) {
//            try {
//                sleep(200);
//                if (AppConstant.CODE_TYPE == AppConstant.ZEROREP) {//置零
//                    serialPort.sendData(Encoder.ZEROREP, "HEX");
//                } else {//3--实时
//                    serialPort.sendData(Encoder.REALINFO, "HEX");
//                }
//
//                int size = serialPort.receiveData(readData);
//                if (size > 0) {
//                    int tmpSize = size * 2;
//                    serialData = SerializeUtil.byteArrayToHexString(readData).substring(0, tmpSize);
//                    Log.i("test", "接收到串口数据:" + serialData);
//                    msg = new Message();
//                    msg.what = AppConstant.READ_TIME_CODE;
//                    msg.obj = serialData;
//                    handlers.sendMessage(msg);
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
//                if (!isReceiveLoop)
//                    colse();
//            }
        }
    }

    public void colse() {
        serialPort.closeSerial();
        handlers.removeCallbacksAndMessages(null);//清空消息队列保证不出现内存泄漏
    }
}
