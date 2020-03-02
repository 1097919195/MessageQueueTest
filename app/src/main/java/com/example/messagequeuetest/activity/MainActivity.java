package com.example.messagequeuetest.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messagequeuetest.R;
import com.example.messagequeuetest.app.AppConstant;
import com.example.messagequeuetest.motherboardsocket.PushBlockQueue;
import com.example.messagequeuetest.util.ToastUtil;
import com.example.x6.serialportlib.SerialPort;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Button sendClearZero, sendRealTime;
    TextView getInfo;
    SerialPort serialttyS0;//串口对象
    int n = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sendClearZero = findViewById(R.id.sendClearZero);
        sendRealTime = findViewById(R.id.sendRealTime);
        getInfo = findViewById(R.id.getInfo);

        initListenter();
        openSrialMotherboard();
        startPushBlockQueue();
    }

    private void initListenter() {
        sendRealTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushBlockQueue.stack.push(AppConstant.REALINFO);
            }
        });

        sendClearZero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushBlockQueue.stack.push(AppConstant.ZEROREP);
            }
        });
    }

    private void startPushBlockQueue() {
        if (serialttyS0 != null) {
            Log.e("队列", "消息队列开启中...");
            PushBlockQueue.getInstance().start(serialttyS0, baseHandler);
            Log.e("队列", "等待队列进入");
        }
    }

    private void openSrialMotherboard() {
        Log.e("serialtty主板", "打开串口中...");
        if (!new File("/dev/ttyS4").exists()) {
            ToastUtil.showLong("请确认串口设置是否正确");
            return;
        }
        serialttyS0 = new SerialPort("S4", 115200, 8, 1, 0);//置零、出秤、入秤
        Log.e("serialtty主板", serialttyS0 == null ? "" : serialttyS0.toString());
        if (serialttyS0 == null) {
            ToastUtil.showLong("主板串口初始化异常！");
        }
    }

    /*实时消息入口*/
    @SuppressLint("HandlerLeak")
    protected Handler baseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AppConstant.READ_TIME_CODE:
                    getInfo.setText(msg.obj.toString());
                    if (PushBlockQueue.stack.empty()) {
                        PushBlockQueue.stack.addMsg(AppConstant.REALINFO);
                    }
                    break;
            }
        }
    };

    private static boolean mBackKeyPressed = false;//记录是否有首次按键

    @Override
    public void onBackPressed() {
        if (!mBackKeyPressed) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            Log.e("返回键触发次数", String.valueOf(n++));
            mBackKeyPressed = true;
            new Timer().schedule(new TimerTask() {//延时两秒，如果超出则擦错第一次按键记录
                @Override
                public void run() {
                    mBackKeyPressed = false;
                }
            }, 2000);
        } else {//退出程序
            this.finish();
//            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PushBlockQueue.getInstance().stop();
        baseHandler.removeCallbacksAndMessages(null);
    }
}
